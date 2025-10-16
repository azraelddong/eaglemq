package com.infoepoch.cmgs.model;

import lombok.Data;

import java.util.List;

@Data
public class EagleMqTopicModel {

    private String topic;

    private CommitLogModel commitLog;

    private List<QueueModel> queueList;

    private Long createTime;

    private Long updateTime;

}
