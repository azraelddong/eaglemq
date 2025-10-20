package com.infoepoch.cmgs.event.model;

import io.netty.channel.ChannelHandlerContext;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class Event {

    private long timeStamp;

    private ChannelHandlerContext ctx;
}
