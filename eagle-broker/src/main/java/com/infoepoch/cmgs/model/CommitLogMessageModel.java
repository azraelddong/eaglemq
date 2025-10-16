package com.infoepoch.cmgs.model;

import com.infoepoch.cmgs.utils.ByteConvertUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * commitLog消息体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommitLogMessageModel {

    /**
     * 消息内容字节
     */
    private byte[] content;
    /**
     * 文件大小，单位：字节
     */
    private int size;

    public byte[] convertToBytes() {
        byte[] sizeByte = ByteConvertUtil.intToByteArray(this.size);
        byte[] contentByte = this.content;

        byte[] mergeByte = new byte[sizeByte.length + contentByte.length];
        System.arraycopy(sizeByte, 0, mergeByte, 0, sizeByte.length);
        System.arraycopy(contentByte, 0, mergeByte, sizeByte.length, contentByte.length);
        return mergeByte;
    }
}
