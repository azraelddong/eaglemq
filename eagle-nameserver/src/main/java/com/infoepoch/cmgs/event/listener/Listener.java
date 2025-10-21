package com.infoepoch.cmgs.event.listener;

import com.infoepoch.cmgs.event.model.Event;

/**
 * 抽象监听事件
 */
public interface Listener<E extends Event> {

    void onEvent(E event);
}
