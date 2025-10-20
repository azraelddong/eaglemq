package com.infoepoch.cmgs.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsumerQueueOffsetModel {

    private OffsetTable offsetTable;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OffsetTable {
        private Map<String, ConsumerGroup> topicConsumerGroup;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ConsumerGroup {
        private Map<String, Map<String, String>> consumerGroup;
    }
}
