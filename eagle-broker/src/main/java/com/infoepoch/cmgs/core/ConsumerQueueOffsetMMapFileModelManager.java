package com.infoepoch.cmgs.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConsumerQueueOffsetMMapFileModelManager {

    private final Map<String, List<ConsumerQueueOffsetMMapFileModel>> map = new HashMap<>();

    public void put(String topic, List<ConsumerQueueOffsetMMapFileModel> consumerQueueOffsetMMapFileModels) {
        map.put(topic, consumerQueueOffsetMMapFileModels);
    }

    public List<ConsumerQueueOffsetMMapFileModel> get(String topic) {
        return map.get(topic);
    }
}
