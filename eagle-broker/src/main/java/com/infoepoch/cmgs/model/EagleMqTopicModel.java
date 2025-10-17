package com.infoepoch.cmgs.model;

import lombok.Data;

import java.util.List;

@Data
public class EagleMqTopicModel {
    /**
     * 主题名称
     */
    private String topic;
    /**
     * commitLog模型
     */
    private CommitLogModel commitLog;
    /**
     * 队列模型
     */
    private List<QueueModel> queueList;
    /**
     * 队列个数
     */
    private Integer queueSize;
    /**
     * 创建时间
     */
    private Long createTime;
    /**
     * 更新时间
     */
    private Long updateTime;

}
