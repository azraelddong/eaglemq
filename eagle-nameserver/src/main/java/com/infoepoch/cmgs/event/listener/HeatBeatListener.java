package com.infoepoch.cmgs.event.listener;

import com.infoepoch.cmgs.coder.TcpMsg;
import com.infoepoch.cmgs.common.CommonCache;
import com.infoepoch.cmgs.enums.ResponseCode;
import com.infoepoch.cmgs.event.model.HeartBeatEvent;
import com.infoepoch.cmgs.store.ServiceInstance;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;

/**
 * 心跳监听器
 */
public class HeatBeatListener implements Listener<HeartBeatEvent> {

    @Override
    public void onEvent(HeartBeatEvent event) {
        ChannelHandlerContext ctx = event.getCtx();
        // 做过认证
        if (ctx.attr(AttributeKey.valueOf("reqId")).get() == null) {
            TcpMsg tcpMsg = new TcpMsg(ResponseCode.ERROR_USER_PWD.getCode(), ResponseCode.ERROR_USER_PWD.getDesc().getBytes());
            ctx.writeAndFlush(tcpMsg);
            ctx.close();
            throw new IllegalArgumentException("error username or password");
        }

        ServiceInstance serviceInstance = new ServiceInstance();
        serviceInstance.setBrokerIp(event.getBrokerIp());
        serviceInstance.setBrokerPort(event.getBrokerPort());
        serviceInstance.setLastRegistrationTime(System.currentTimeMillis());
        CommonCache.getServiceInstanceManager().putIfAbsent(serviceInstance);
    }
}
