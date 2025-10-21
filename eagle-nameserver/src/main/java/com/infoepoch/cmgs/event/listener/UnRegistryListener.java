package com.infoepoch.cmgs.event.listener;

import com.infoepoch.cmgs.coder.TcpMsg;
import com.infoepoch.cmgs.common.CommonCache;
import com.infoepoch.cmgs.enums.ResponseCode;
import com.infoepoch.cmgs.event.model.UnRegistryEvent;
import com.infoepoch.cmgs.store.ServiceInstance;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import io.netty.util.internal.StringUtil;

/**
 * 下线监听器
 */
public class UnRegistryListener implements Listener<UnRegistryEvent> {

    @Override
    public void onEvent(UnRegistryEvent event) {
        ChannelHandlerContext ctx = event.getCtx();
        if (ctx.attr(AttributeKey.valueOf("reqId")) == null) {
            TcpMsg tcpMsg = new TcpMsg(ResponseCode.ERROR_USER_PWD.getCode(), ResponseCode.ERROR_USER_PWD.getDesc().getBytes());
            ctx.writeAndFlush(tcpMsg);
            ctx.close();
            throw new IllegalArgumentException("error username or password");
        }

        //移除serviceInstance实例
        String brokerIp = event.getBrokerIp();
        Integer brokerPort = event.getBrokerPort();
        if (!StringUtil.isNullOrEmpty(brokerIp) && brokerPort != null) {
            ServiceInstance oldServiceInstance = CommonCache.getServiceInstanceManager().remove(brokerIp, brokerPort);
            if (oldServiceInstance != null) {
                TcpMsg tcpMsg = new TcpMsg(ResponseCode.UN_REGISTRY.getCode(), ResponseCode.UN_REGISTRY.getDesc().getBytes());
                ctx.writeAndFlush(tcpMsg);
                ctx.close();
            }
        }
    }

}
