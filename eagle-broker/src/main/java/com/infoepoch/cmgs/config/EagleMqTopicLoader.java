package com.infoepoch.cmgs.config;

import com.alibaba.fastjson.JSON;
import com.infoepoch.cmgs.cache.CommonCache;
import com.infoepoch.cmgs.constants.BrokerConstants;
import com.infoepoch.cmgs.model.EagleMqTopicModel;
import com.infoepoch.cmgs.utils.FileUtil;
import io.netty.util.internal.StringUtil;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 启动加载mq主配置到内存
 */
public class EagleMqTopicLoader {

    private String filePath;

    public void load() {
        GlobalProperties globalProperties = CommonCache.getGlobalProperties();
        String basePath = globalProperties.getEagleMqHome();
        if (StringUtil.isNullOrEmpty(basePath)) {
            throw new IllegalArgumentException("eagle mq home is invalid");
        }
        filePath = basePath + "/broker/config/eaglemq-topic.json";
        String content = FileUtil.read(filePath);
        List<EagleMqTopicModel> models = JSON.parseArray(content, EagleMqTopicModel.class);
        CommonCache.setTopicModelList(models);
    }

    /**
     * 开启一个线程任务
     */
    public void eagleMqTopicInfoTask() {
        // 异步线程
        // 每隔15s将内存中的配置刷新到磁盘中
        CommonThreadPoolConfig.executor.execute(() -> {
            while (true) {
                try {
                    TimeUnit.SECONDS.sleep(BrokerConstants.DEFAULT_TIME_STAMP);
                    System.out.println("========>开始刷新磁盘");
                    List<EagleMqTopicModel> topicModelList = CommonCache.getTopicModelList();
                    FileUtil.write(filePath, JSON.toJSONString(topicModelList));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
