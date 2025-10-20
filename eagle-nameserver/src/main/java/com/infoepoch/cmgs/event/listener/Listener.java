package com.infoepoch.cmgs.event.listener;

import com.infoepoch.cmgs.event.model.Event;

/**
 * 监听者
 */
public interface Listener<E extends Event> {

    void onEvent(E event);
}
