package com.infoepoch.cmgs.config;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class CommonThreadPoolConfig {

    public static ThreadPoolExecutor executor =
            new ThreadPoolExecutor(
                    1,
                    1,
                    30,
                    TimeUnit.SECONDS,
                    new ArrayBlockingQueue<>(10),
                    r -> {
                        Thread thread = new Thread(r);
                        thread.setName("refresh-eagle-mq-topic-config");
                        thread.setDaemon(true);
                        return thread;
                    }
            );
}
