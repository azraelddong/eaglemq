package com.infoepoch.cmgs.model;

import lombok.Data;

import java.util.concurrent.atomic.AtomicInteger;

@Data
public class CommitLogModel {
    /**
     * 最新的commitLog文件名称
     */
    private String fileName;
    /**
     * 最新的offset值
     */
    private AtomicInteger offset;
    /**
     * commitLog文件偏移量限制的容量
     */
    private Long offsetLimit;

    public Long countDiff() {
        return this.offsetLimit - this.offset.get();
    }
}
