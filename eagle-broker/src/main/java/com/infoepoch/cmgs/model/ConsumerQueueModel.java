package com.infoepoch.cmgs.model;

import com.infoepoch.cmgs.utils.ByteConvertUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;

/**
 * consumerQueue数据存储的最小单元对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsumerQueueModel {
    /**
     * commitLog文件名称
     */
    private int fileName;
    /**
     * 消息开始下标
     */
    private int msgIndex;
    /**
     * 消息长度
     */
    private int msgLength;

    public byte[] convertToBytes() {
        byte[] fileNameBytes = ByteConvertUtil.intToByteArray(this.fileName);
        byte[] msgIndexBytes = ByteConvertUtil.intToByteArray(this.msgIndex);
        byte[] msgLengthBytes = ByteConvertUtil.intToByteArray(this.msgLength);

        byte[] mergeByte = new byte[12];
        System.arraycopy(fileNameBytes, 0, mergeByte, 0, fileNameBytes.length);
        System.arraycopy(msgIndexBytes, 0, mergeByte, fileNameBytes.length, msgIndexBytes.length);
        System.arraycopy(msgLengthBytes, 0, mergeByte, fileNameBytes.length + msgIndexBytes.length, msgLengthBytes.length);
        return mergeByte;
    }

    public ConsumerQueueModel convertToModel(byte[] content) {
        // 提取各个字段的字节段
        byte[] fileNameBytes = Arrays.copyOfRange(content, 0, 4);
        byte[] msgIndexBytes = Arrays.copyOfRange(content, 4, 8);
        byte[] msgLengthBytes = Arrays.copyOfRange(content, 8, 12);

        // 转换为int值
        this.fileName =  ByteConvertUtil.byteArrayToInt(fileNameBytes);
        this.msgIndex =  ByteConvertUtil.byteArrayToInt(msgIndexBytes);
        this.msgLength =  ByteConvertUtil.byteArrayToInt(msgLengthBytes);
        return this;
    }
}
