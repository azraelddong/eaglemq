package com.infoepoch.cmgs.core;

import com.infoepoch.cmgs.cache.CommonCache;
import com.infoepoch.cmgs.model.ConsumerQueueModel;
import com.infoepoch.cmgs.model.ConsumerQueueOffsetModel;
import com.infoepoch.cmgs.model.EagleMqTopicModel;
import com.infoepoch.cmgs.model.QueueModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 消息队列的消费处理器
 */
public class ConsumerQueueConsumeHandler {

    /**
     * 读取当前最新一条消息
     *
     * @param topic   主题
     * @param group   消费组
     * @param queueId 消费队列id
     * @return 消费队列的消息内容
     */
    public byte[] consume(String topic, String group, Integer queueId) {
        ConsumerQueueOffsetModel consumerQueueOffsetModel = CommonCache.getConsumerQueueModel();
        if (consumerQueueOffsetModel == null)
            throw new RuntimeException("ConsumerQueueOffsetModel is null");
        ConsumerQueueOffsetModel.OffsetTable offsetTable = CommonCache.getConsumerQueueModel().getOffsetTable();
        if (offsetTable == null)
            throw new RuntimeException("offsetTable is null");
        Map<String, ConsumerQueueOffsetModel.ConsumerGroup> topicConsumerGroupMap = offsetTable.getTopicConsumerGroup();
        ConsumerQueueOffsetModel.ConsumerGroup consumerGroup = topicConsumerGroupMap.get(topic);
        if (consumerGroup == null) {
            consumerGroup = new ConsumerQueueOffsetModel.ConsumerGroup();
            topicConsumerGroupMap.put(topic, consumerGroup);
        }
        Map<String, Map<String, String>> consumerGroupMap = consumerGroup.getConsumerGroup();
        Map<String, String> map = consumerGroupMap.get(group);
        if (map == null) {
            map = new HashMap<>();
            EagleMqTopicModel eagleMqTopicModel = CommonCache.getTopicModelMap().get(topic);
            if (eagleMqTopicModel == null)
                throw new RuntimeException("EagleMqTopicModel is null");
            List<QueueModel> queueList = eagleMqTopicModel.getQueueList();
            for (QueueModel queueModel : queueList) {
                map.put(String.valueOf(queueModel.getId()), "00000000#0");
            }
            consumerGroupMap.put(group, map);
        }
        String offsetInfo = map.get(String.valueOf(queueId));
        String[] offsetInfoArr = offsetInfo.split("#");
//        String fileName = offsetInfoArr[0];
        String offset = offsetInfoArr[1];

        List<ConsumerQueueOffsetMMapFileModel> consumerQueueOffsetMMapFileModels = CommonCache.getConsumerQueueOffsetMMapFileModelManager().get(topic);
        ConsumerQueueOffsetMMapFileModel consumerQueueOffsetMMapFileModel = consumerQueueOffsetMMapFileModels.get(queueId);
        byte[] content = consumerQueueOffsetMMapFileModel.read(Integer.parseInt(offset));

        ConsumerQueueModel consumerQueueModel = new ConsumerQueueModel();
        consumerQueueModel.convertToModel(content);

        MMapFileModel mMapFileModel = CommonCache.getmMapFileModelManager().get(topic);
        return mMapFileModel.read(consumerQueueModel.getMsgIndex(), consumerQueueModel.getMsgLength());
    }

    /**
     * 消息消费完成，更新consume-queue中offset值
     *
     * @return 消费完成
     */
    public boolean ack(String topic, String group, Integer queueId) {
        ConsumerQueueOffsetModel.OffsetTable offsetTable = CommonCache.getConsumerQueueModel().getOffsetTable();
        Map<String, ConsumerQueueOffsetModel.ConsumerGroup> topicConsumerGroupMap = offsetTable.getTopicConsumerGroup();
        ConsumerQueueOffsetModel.ConsumerGroup consumerGroup = topicConsumerGroupMap.get(topic);
        Map<String, Map<String, String>> consumerGroupMap = consumerGroup.getConsumerGroup();
        Map<String, String> map = consumerGroupMap.get(group);
        String offsetInfo = map.get(String.valueOf(queueId));
        String[] attr = offsetInfo.split("#");
        String fileName = attr[0];
        int offset = Integer.parseInt(attr[1]);
        offset += 12;   // 更新offset值
        map.put(String.valueOf(queueId), fileName + "#" + offset);
        return true;
    }
}
