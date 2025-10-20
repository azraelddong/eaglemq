package com.infoepoch.cmgs.event.listener;

import com.infoepoch.cmgs.event.model.Event;

public interface Listener<E extends Event> {

    void onEvent(E event);
}
