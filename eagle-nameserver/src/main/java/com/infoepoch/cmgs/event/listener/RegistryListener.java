package com.infoepoch.cmgs.event.listener;

import com.infoepoch.cmgs.coder.TcpMsg;
import com.infoepoch.cmgs.common.CommonCache;
import com.infoepoch.cmgs.enums.ResponseCode;
import com.infoepoch.cmgs.event.model.RegistryEvent;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;

import java.util.Objects;
import java.util.UUID;

public class RegistryListener implements Listener<RegistryEvent> {

    @Override
    public void onEvent(RegistryEvent event) {
        String username = event.getUsername();
        String password = event.getPassword();

        String user = CommonCache.getPropertiesLoader().getProperties("nameserver.username");
        String pwd = CommonCache.getPropertiesLoader().getProperties("nameserver.password");

        // 认证校验
        ChannelHandlerContext ctx = event.getCtx();
        if (!Objects.equals(user, username) || !Objects.equals(password, pwd)) {
            TcpMsg tcpMsg = new TcpMsg(ResponseCode.ERROR_USER_PWD.getCode(), ResponseCode.ERROR_USER_PWD.getDesc().getBytes());
            ctx.writeAndFlush(tcpMsg);
            ctx.close();
            throw new IllegalArgumentException("error username or password");
        }

        ctx.attr(AttributeKey.valueOf("reqId")).set(UUID.randomUUID().toString());
    }
}
