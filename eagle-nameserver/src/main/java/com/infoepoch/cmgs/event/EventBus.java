package com.infoepoch.cmgs.event;

import com.google.common.collect.Lists;
import com.infoepoch.cmgs.event.listener.Listener;
import com.infoepoch.cmgs.event.model.Event;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 事件总线
 */
public class EventBus {

    private static final Map<Class<? extends Event>, List<Listener<? extends Event>>> listenerMap = new ConcurrentHashMap<>();

    public void init() {

    }

    public <E extends Event> void registry(Class<? extends Event> clazz, Listener<E> listener) {
        List<Listener<? extends Event>> listeners = listenerMap.get(clazz);
        if (listeners == null) {
            listenerMap.put(clazz, Lists.newArrayList(listener));
        } else {
            listeners.add(listener);
            listenerMap.put(clazz, listeners);
        }
    }

    public void publish(Event event) {

    }
}
