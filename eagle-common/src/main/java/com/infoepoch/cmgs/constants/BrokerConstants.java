package com.infoepoch.cmgs.constants;

public class BrokerConstants {

    public static final String EAGLE_MQ_HOME = "eaglemq_home";

    public static final String BROKER_PROPERTIES_HOME = "/broker/config/broker.properties";

    public static final String BASE_TOPIC_PATH = "/broker/store/";

    public static final String BASE_CONSUMER_QUEUE_PATH = "/broker/consumerqueue/";

    public static final String FILE_SEPARATE = "/";

    public static final int MMAP_SIZE = 1024 * 1024;    // 1mb

    public static final int REFRESH_TOPIC_INFO_TIME_STAMP = 3;

    public static final int REFRESH_CONSUMER_QUEUE_OFFSET_TIME_STAMP = 3;

    public static final short DEFAULT_MAGIC_NUM = 1781;
}
