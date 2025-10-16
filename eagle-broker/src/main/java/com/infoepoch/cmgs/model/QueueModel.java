package com.infoepoch.cmgs.model;

import lombok.Data;

@Data
public class QueueModel {

    private Integer id;

    private Long minOffset;

    private Long currentOffset;

    private Long maxOffset;
}
