package com.infoepoch.cmgs.core;

import java.util.HashMap;
import java.util.Map;

public class MMapFileModelManager {

    private Map<String, MMapFileModel> map = new HashMap<>();

    public void put(String topic, MMapFileModel mapFileModel) {
        map.put(topic, mapFileModel);
    }

    public MMapFileModel get(String topic) {
        return map.get(topic);
    }
}
