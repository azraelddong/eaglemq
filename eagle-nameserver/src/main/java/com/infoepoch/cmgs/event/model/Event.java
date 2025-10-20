package com.infoepoch.cmgs.event.model;

import io.netty.channel.ChannelHandlerContext;

public abstract class Event {

    private long timeStamp;

    private ChannelHandlerContext ctx;
}
