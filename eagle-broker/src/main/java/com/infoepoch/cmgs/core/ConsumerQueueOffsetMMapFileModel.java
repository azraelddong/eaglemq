package com.infoepoch.cmgs.core;

import com.infoepoch.cmgs.cache.CommonCache;
import com.infoepoch.cmgs.model.EagleMqTopicModel;
import com.infoepoch.cmgs.model.QueueModel;
import com.infoepoch.cmgs.utils.LogFileNameUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.infoepoch.cmgs.utils.LogFileNameUtil.appendConsumerQueueOffsetFilePath;

public class ConsumerQueueOffsetMMapFileModel {

    private File file;

    private MappedByteBuffer mappedByteBuffer;

    private ByteBuffer readBuffer;

    private FileChannel fileChannel;

    private String topicName;

    private Integer queueId;

    private Lock lock;

    public String getTopicName() {
        return this.topicName;
    }

    public Integer getQueueId() {
        return this.queueId;
    }

    /**
     * 加载文件
     */
    public void load(String topic, Integer queueId, int startOffset, int mappedSize) throws IOException {
        this.topicName = topic;
        this.queueId = queueId;
        this.lock = new ReentrantLock();
        String filePath = getLatestConsumerQueueFilePath();
        doMmap(filePath, startOffset, mappedSize);
    }

    private void doMmap(String filePath, int startOffset, int mappedSize) throws IOException {
        this.file = new File(filePath);
        if (!this.file.exists()) {
            throw new FileNotFoundException("文件路径：" + filePath + "，没有找到对应文件");
        }
        this.fileChannel = new RandomAccessFile(this.file, "rw").getChannel();
        this.mappedByteBuffer = this.fileChannel.map(FileChannel.MapMode.READ_WRITE, startOffset, mappedSize);
        this.readBuffer = this.mappedByteBuffer.slice();
    }

    /**
     * 获取当前最新的consumerQueue文件路径
     *
     * @return 最新的consumerQueue文件路径
     */
    private String getLatestConsumerQueueFilePath() {
        // 获取commitLog文件名称
        EagleMqTopicModel eagleMqTopicModel = CommonCache.getTopicModelMap().get(this.topicName);
        if (eagleMqTopicModel == null) {
            throw new IllegalArgumentException("topic [" + this.topicName + "] does not exist");
        }
        QueueModel queueModel =
                eagleMqTopicModel.getQueueList().stream()
                        .filter(s -> Objects.equals(queueId, s.getId()))
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("queue [" + queueId + "] does not exist"));

        String filePath = null;
        // 判断当前offset是否超过了offset限制
        long diff = queueModel.getDiff();
        if (diff == 0) {
            // 创建新commitLog文件
            filePath = createNewConsumerQueueOffsetFilePath(queueModel);
        } else if (diff > 0) {
            filePath = appendConsumerQueueOffsetFilePath(this.topicName, queueId, queueModel.getFileName());
        }

        return filePath;
    }

    /**
     * 创新新的commitLog文件
     *
     * @param queueModel 消息队列对象
     * @return 新文件路径
     */
    private String createNewConsumerQueueOffsetFilePath(QueueModel queueModel) {
        String newFileName = LogFileNameUtil.incrCommitLogFileName(queueModel.getFileName());
        String newFilePath = appendConsumerQueueOffsetFilePath(this.topicName, this.queueId, newFileName);

        File file = new File(newFilePath);
        try {
            file.createNewFile();
            System.out.println("==========> 创建了一个新的consumerQueue文件");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return newFilePath;
    }

    public void write(byte[] content) {
        write(content, false);
    }

    public void write(byte[] content, boolean isForce) {
        lock.lock();
        try {
            mappedByteBuffer.put(content);
            if (isForce) {
                mappedByteBuffer.force();
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * 读取
     * @param pos 定位
     * @return
     */
    public byte[] read(int pos) {
        // slice：开启一个窗口
        ByteBuffer buffer = this.readBuffer.slice();
        buffer.position(pos);
        byte[] content = new byte[12];
        buffer.get(content);
        return content;
    }
}
