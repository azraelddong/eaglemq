package com.infoepoch.cmgs.config;

import com.alibaba.fastjson.JSON;
import com.infoepoch.cmgs.cache.CommonCache;
import com.infoepoch.cmgs.model.EagleMqTopicModel;
import com.infoepoch.cmgs.utils.FileUtil;
import io.netty.util.internal.StringUtil;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 启动加载mq主配置到内存
 */
public class EagleMqTopicLoader {

    public void load() {
        GlobalProperties globalProperties = CommonCache.getGlobalProperties();
        String basePath = globalProperties.getEagleMqHome();
        if (StringUtil.isNullOrEmpty(basePath)) {
            throw new IllegalArgumentException("eagle mq home is invalide");
        }
        String path = basePath + "/broker/config/eaglemq-topic.json";
        String content = FileUtil.read(path);
        List<EagleMqTopicModel> models = JSON.parseArray(content, EagleMqTopicModel.class);
        CommonCache.setTopicModelMap(models.stream().collect(Collectors.toMap(EagleMqTopicModel::getTopic, s -> s)));
    }
}
