package com.infoepoch.cmgs.model;

import lombok.Data;

import java.util.concurrent.atomic.AtomicInteger;

@Data
public class QueueModel {

    private Integer id;

    private String fileName;

    private int offsetLimit;

    private AtomicInteger latestOffset;

    private int lastOffset;

    public int getDiff() {
        return this.offsetLimit - this.latestOffset.get();
    }
}
