package com.infoepoch.cmgs.core;

import com.infoepoch.cmgs.cache.CommonCache;
import com.infoepoch.cmgs.constants.BrokerConstants;
import com.infoepoch.cmgs.model.CommitLogMessageModel;
import com.infoepoch.cmgs.model.CommitLogModel;
import com.infoepoch.cmgs.model.ConsumerQueueModel;
import com.infoepoch.cmgs.model.EagleMqTopicModel;
import com.infoepoch.cmgs.model.QueueModel;
import com.infoepoch.cmgs.utils.LogFileNameUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sun.nio.ch.FileChannelImpl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Method;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.infoepoch.cmgs.utils.LogFileNameUtil.appendCommitLogFilePath;

public class MMapFileModel {

    private File file;

    private MappedByteBuffer mappedByteBuffer;

    private FileChannel fileChannel;

    private String topicName;

    private Lock lock;

    /**
     * 加载文件
     *
     * @param topic       主题名称
     * @param startOffset 开始偏移量
     * @param mappedSize  映射大小
     * @throws IOException IO异常
     */
    public void load(String topic, int startOffset, int mappedSize) throws IOException {
        this.topicName = topic;
        this.lock = new ReentrantLock();
        String filePath = getLatestCommitLogFilePath();
        doMmap(filePath, startOffset, mappedSize);
    }

    private void doMmap(String filePath, int startOffset, int mappedSize) throws IOException {
        this.file = new File(filePath);
        if (!this.file.exists()) {
            throw new FileNotFoundException("文件路径：" + filePath + "，没有找到对应文件");
        }
        this.fileChannel = new RandomAccessFile(this.file, "rw").getChannel();
        this.mappedByteBuffer = this.fileChannel.map(FileChannel.MapMode.READ_WRITE, startOffset, mappedSize);
    }

    /**
     * 获取当前最新的commitLog文件路径
     *
     * @return 最新的commitLog文件路径
     */
    private String getLatestCommitLogFilePath() {
        // 获取commitLog文件名称
        EagleMqTopicModel eagleMqTopicModel = CommonCache.getTopicModelMap().get(this.topicName);
        CommitLogModel commitLog = eagleMqTopicModel.getCommitLog();

        String filePath = null;
        // 判断当前offset是否超过了offset限制
        long diff = commitLog.countDiff();
        if (diff == 0) {
            // 创建新commitLog文件
            CommitLogFilePath commitLogFilePath = createNewCommitLogFile(commitLog);
            filePath = commitLogFilePath.getFilePath();
        } else if (diff > 0) {
            filePath = appendCommitLogFilePath(this.topicName, commitLog.getFileName());
        }

        return filePath;
    }

    /**
     * 创新新的commitLog文件
     *
     * @param commitLog commitLog对象
     * @return 新commitLog文件路径
     */
    private CommitLogFilePath createNewCommitLogFile(CommitLogModel commitLog) {
        String newFileName = LogFileNameUtil.incrCommitLogFileName(commitLog.getFileName());
        String newFilePath = appendCommitLogFilePath(this.topicName, newFileName);

        File file = new File(newFilePath);
        try {
            file.createNewFile();
            System.out.println("==========> 创建了一个新的文件");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new CommitLogFilePath(newFileName, newFilePath);
    }

    /**
     * 读取
     *
     * @param offset 开始读取偏移量
     * @param size   读取大小
     * @return 待读取的内容
     */
    public byte[] read(int offset, int size) {
        this.mappedByteBuffer.position(offset); // 定位开始读取的位置
        byte[] content = new byte[size];

        int j = 0;
        for (int i = 0; i < size; i++) {
            content[j++] = this.mappedByteBuffer.get(offset + i);   // 直接读取内存数据，很快！
        }
        return content;
    }

    /**
     * 写入数据，默认不强制刷盘
     */
    public void write(CommitLogMessageModel commitLogMessageModel) throws IOException {
        write(commitLogMessageModel, false);
    }

    /**
     * 写数据
     *
     * @param commitLogMessageModel commitLog消息体
     * @param isForce               是否强制刷盘
     */
    public void write(CommitLogMessageModel commitLogMessageModel, boolean isForce) throws IOException {
        // commitLog是否写满问题？
        // 如何管理topic的最新写入offset值，定时刷盘操作
        // 线程安全问题：多线程更改offset？顺序问题？

        EagleMqTopicModel eagleMqTopicModel = CommonCache.getTopicModelMap().get(this.topicName);
        if (eagleMqTopicModel == null) {
            throw new IllegalArgumentException("eagleMqTopicModel is null");
        }
        CommitLogModel commitLog = eagleMqTopicModel.getCommitLog();
        if (commitLog == null) {
            throw new IllegalArgumentException("commitLog is null");
        }

        // 校验commitLog文件剩余容量是否够存储？
        validateCommitLogSpace(commitLogMessageModel);

        // 将消息大小4个字节放在内容最前面
        byte[] writeContent = commitLogMessageModel.convertToBytes();
        lock.lock();
        try {
            this.mappedByteBuffer.put(writeContent);

            AtomicInteger offset = commitLog.getOffset();
            // 🌈消息分发
            dispatcher(writeContent.length, offset.get());

            offset.addAndGet(writeContent.length);
            if (isForce) {
                this.mappedByteBuffer.force();  // 强制刷盘数据
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }

    }

    private void dispatcher(int msgLength, int offset) {

        // 获取commitLog文件名称
        EagleMqTopicModel eagleMqTopicModel = CommonCache.getTopicModelMap().get(this.topicName);
        CommitLogModel commitLog = eagleMqTopicModel.getCommitLog();

        ConsumerQueueModel consumerQueueModel =
                new ConsumerQueueModel(
                        Integer.parseInt(commitLog.getFileName()),
                        offset,
                        msgLength);

        byte[] content = consumerQueueModel.convertToBytes();

        //todo: 后期优化，暂时写死
        int queueId = 0;

        List<ConsumerQueueOffsetMMapFileModel> consumerQueueOffsetMMapFileModels = CommonCache.getConsumerQueueOffsetMMapFileModelManager().get(this.topicName);
        ConsumerQueueOffsetMMapFileModel consumerQueueOffsetMMapFileModel =
                consumerQueueOffsetMMapFileModels.stream()
                        .filter(s -> Objects.equals(s.getQueueId(), queueId))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("queueId is invalid."));

        consumerQueueOffsetMMapFileModel.write(content);

        // 更新offset
        QueueModel queueModel = eagleMqTopicModel.getQueueList().get(queueId);
        queueModel.getLatestOffset().addAndGet(content.length);
    }

    /**
     * 校验commitLog文件空间,空间不足，创建新的commitLog文件，并进行映射
     */
    private void validateCommitLogSpace(CommitLogMessageModel commitLogMessageModel) throws IOException {
        EagleMqTopicModel eagleMqTopicModel = CommonCache.getTopicModelMap().get(this.topicName);
        CommitLogModel commitLog = eagleMqTopicModel.getCommitLog();
        if (commitLog.countDiff() < commitLogMessageModel.getSize()) {
            // 剩余空间放不下，创建新的commitLog文件
            CommitLogFilePath commitLogFilePath = createNewCommitLogFile(commitLog);

            // 重置commitLog文件
            commitLog.setOffset(new AtomicInteger(0));
            commitLog.setOffsetLimit((long) BrokerConstants.MMAP_SIZE);
            commitLog.setFileName(commitLogFilePath.getFileName());

            // 将文件映射出去
            doMmap(commitLogFilePath.getFilePath(), 0, BrokerConstants.MMAP_SIZE);
        }
    }

    /**
     * 清理mapped内存
     */
    public void clean() throws Exception {
        Method method = FileChannelImpl.class.getDeclaredMethod("unmap", MappedByteBuffer.class);
        method.setAccessible(true);
        method.invoke(FileChannelImpl.class, this.mappedByteBuffer);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class CommitLogFilePath {
        private String fileName;
        private String filePath;
    }
}
