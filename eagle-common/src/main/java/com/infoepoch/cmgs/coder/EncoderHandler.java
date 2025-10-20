package com.infoepoch.cmgs.coder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 编码器
 */
public class EncoderHandler extends MessageToByteEncoder {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {
        TcpMsg msg = (TcpMsg) o;
        byteBuf.writeShort(msg.getMagic());
        byteBuf.writeInt(msg.getCode());
        byteBuf.writeInt(msg.getLen());
        byteBuf.writeBytes(msg.getData());
    }
}
