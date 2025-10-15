package com.infoepoch.cmgs.core;

import sun.nio.ch.FileChannelImpl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Method;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class MMapFileModel {
    private File file;
    private MappedByteBuffer mappedByteBuffer;
    private FileChannel fileChannel;

    /**
     * 加载文件
     *
     * @param filePath    文件路径
     * @param startOffset 开始偏移量
     * @param mappedSize  映射大小
     * @throws IOException IO异常
     */
    public void load(String filePath, int startOffset, int mappedSize) throws IOException {
        this.file = new File(filePath);
        if (!this.file.exists()) {
            throw new FileNotFoundException("文件路径：" + filePath + "，没有找到对应文件");
        }
        this.fileChannel = new RandomAccessFile(this.file, "rw").getChannel();
        this.mappedByteBuffer = this.fileChannel.map(FileChannel.MapMode.READ_WRITE, startOffset, mappedSize);
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
     * 写数据
     *
     * @param content 待写入内容数据
     * @param isForce 是否强制刷盘
     */
    public void write(byte[] content, boolean isForce) {
        this.mappedByteBuffer.put(content);
        if (isForce) {
            this.mappedByteBuffer.force();  // 强制刷盘数据
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
}
