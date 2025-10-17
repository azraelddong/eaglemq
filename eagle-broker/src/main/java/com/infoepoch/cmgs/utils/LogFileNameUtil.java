package com.infoepoch.cmgs.utils;

import com.infoepoch.cmgs.cache.CommonCache;
import com.infoepoch.cmgs.constants.BrokerConstants;
import io.netty.util.internal.StringUtil;

/**
 * commitLog 文件名称生成工具类
 */
public class LogFileNameUtil {

    /**
     * 拼接commitLog文件路径
     *
     * @param topic    主题
     * @param fileName commitLog文件名称
     * @return commitLog路径
     */
    public static String appendCommitLogFilePath(String topic, String fileName) {
        return CommonCache.getGlobalProperties().getEagleMqHome()
                + BrokerConstants.BASE_TOPIC_PATH
                + topic
                + BrokerConstants.FILE_SEPARATE
                + fileName;
    }

    /**
     * 拼接consumerQueueOffset文件路径
     *
     * @param topic    主题
     * @param queueId  队列id
     * @param fileName 文件名称
     * @return consumerQueueOffset文件路径
     */
    public static String appendConsumerQueueOffsetFilePath(String topic, Integer queueId, String fileName) {
        return CommonCache.getGlobalProperties().getEagleMqHome()
                + BrokerConstants.BASE_CONSUMER_QUEUE_PATH
                + topic
                + BrokerConstants.FILE_SEPARATE
                + queueId
                + BrokerConstants.FILE_SEPARATE
                + fileName;
    }

    /**
     * 构建初始commitLog文件名称
     *
     * @return 00000000
     */
    public static String buildCommitLogFileName() {
        return "00000000";
    }

    /**
     * 递增commitLog文件名称
     *
     * @param oldCommitLogFileName 旧commitLog文件名称
     * @return 新commitLog文件名称
     */
    public static String incrCommitLogFileName(String oldCommitLogFileName) {
        if (StringUtil.isNullOrEmpty(oldCommitLogFileName)
                || !oldCommitLogFileName.matches("\\d{8}")) {
            throw new IllegalArgumentException("Invalid commit log file name: " + oldCommitLogFileName);
        }

        long num = Long.parseLong(oldCommitLogFileName);
        num++;
        if (num > 99999999) {
            throw new RuntimeException("Commit log file name too big: " + oldCommitLogFileName);
        }

        return String.format("%08d", num);
    }
}
