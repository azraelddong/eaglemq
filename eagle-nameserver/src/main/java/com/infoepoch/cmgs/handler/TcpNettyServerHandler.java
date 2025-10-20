package com.infoepoch.cmgs.handler;

import com.alibaba.fastjson.JSON;
import com.infoepoch.cmgs.coder.TcpMsg;
import com.infoepoch.cmgs.enums.EventCodeEnum;
import com.infoepoch.cmgs.event.EventBus;
import com.infoepoch.cmgs.event.model.Event;
import com.infoepoch.cmgs.event.model.HeartBeatEvent;
import com.infoepoch.cmgs.event.model.RegistryEvent;
import com.infoepoch.cmgs.event.model.UnRegistryEvent;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@ChannelHandler.Sharable
public class TcpNettyServerHandler extends SimpleChannelInboundHandler {

    private final EventBus eventBus;

    public TcpNettyServerHandler(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        TcpMsg tcpMsg = (TcpMsg) o;
        int code = tcpMsg.getCode();
        byte[] data = tcpMsg.getData();

        Event event = null;
        if (EventCodeEnum.REGISTRY.getCode() == code) {
            // 上线事件
            event = JSON.parseObject(data, RegistryEvent.class);
        } else if (EventCodeEnum.UN_REGISTRY.getCode() == code) {
            // 下线事件
            event = JSON.parseObject(data, UnRegistryEvent.class);
        } else if (EventCodeEnum.HEART_BEAT.getCode() == code) {
            // 心跳事件
            event = JSON.parseObject(data, HeartBeatEvent.class);
        }
        eventBus.publish(event);
    }
}
