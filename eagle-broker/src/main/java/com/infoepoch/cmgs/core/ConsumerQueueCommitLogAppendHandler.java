package com.infoepoch.cmgs.core;

import com.infoepoch.cmgs.cache.CommonCache;
import com.infoepoch.cmgs.model.EagleMqTopicModel;
import com.infoepoch.cmgs.model.QueueModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 消费队列commitLog写入处理器
 */
public class ConsumerQueueCommitLogAppendHandler {

    public void prepareLoading(String topic) throws IOException {
        EagleMqTopicModel eagleMqTopicModel = CommonCache.getTopicModelMap().get(topic);
        List<QueueModel> queueList = eagleMqTopicModel.getQueueList();

        List<ConsumerQueueOffsetMMapFileModel> consumerQueueOffsetMMapFileModels = new ArrayList<>();
        for (QueueModel queueModel : queueList) {
            ConsumerQueueOffsetMMapFileModel consumerQueueOffsetMMapFileModel = new ConsumerQueueOffsetMMapFileModel();
            consumerQueueOffsetMMapFileModel.load(
                    topic,
                    queueModel.getId(),
                    queueModel.getLastOffset(),
                    queueModel.getOffsetLimit());
            consumerQueueOffsetMMapFileModels.add(consumerQueueOffsetMMapFileModel);
        }

        CommonCache.getConsumerQueueOffsetMMapFileModelManager().put(topic, consumerQueueOffsetMMapFileModels);
    }
}
