package com.infoepoch.cmgs.coder;

import com.infoepoch.cmgs.constants.BrokerConstants;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * 解码器
 */
public class DecoderHandler extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> list) throws Exception {
        if (byteBuf.readableBytes() > 2 + 4 + 4) {
            short magic = byteBuf.readShort();
            if (magic != BrokerConstants.DEFAULT_MAGIC_NUM) {
                ctx.close();
                return;
            }
            int code = byteBuf.readInt();
            int leg = byteBuf.readInt();
            if (byteBuf.readableBytes() < leg) {
                ctx.close();
                return;
            }
            byte[] data = new byte[leg];
            byteBuf.readBytes(data);
            TcpMsg tcpMsg = new TcpMsg(magic, code, leg, data);
            list.add(tcpMsg);
        }
    }
}
