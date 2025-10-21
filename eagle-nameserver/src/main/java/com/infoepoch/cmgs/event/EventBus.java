package com.infoepoch.cmgs.event;

import com.google.common.collect.Lists;
import com.infoepoch.cmgs.event.listener.Listener;
import com.infoepoch.cmgs.event.model.Event;
import com.infoepoch.cmgs.utils.ReflectUtil;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 事件总线
 */
public class EventBus {

    private static final Map<Class<? extends Event>, List<Listener>> listenerMap = new ConcurrentHashMap<>();

    private ThreadPoolExecutor executor = new ThreadPoolExecutor(
            10,
            100,
            3,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(1000),
            r -> {
                Thread thread = new Thread(r);
                thread.setName("event-bus-task" + thread.getId());
                return thread;
            }
    );


    public void init() {
        // 基于SPI机制来获取listener实现类
        ServiceLoader<Listener> serviceLoader = ServiceLoader.load(Listener.class);
        for (Listener listener : serviceLoader) {
            Class clazz = ReflectUtil.getInterfaceT(listener, 0);
            this.registry(clazz, listener);
        }
    }

    public <E extends Event> void registry(Class<? extends Event> clazz, Listener listener) {
        List<Listener> listeners = listenerMap.get(clazz);
        if (CollectionUtils.isEmpty(listeners)) {
            listenerMap.put(clazz, Lists.newArrayList(listener));
        } else {
            listeners.add(listener);
            listenerMap.put(clazz, listeners);
        }
    }

    public <E extends Event> void publish(E event) {
        List<Listener> listeners = listenerMap.get(event.getClass());
        executor.execute(() -> {
            try {
                for (Listener listener : listeners) {
                    listener.onEvent(event);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
