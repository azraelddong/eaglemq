package com.infoepoch.cmgs.netty;

import com.infoepoch.cmgs.coder.DecoderHandler;
import com.infoepoch.cmgs.coder.EncoderHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NameServerClient {

    NioEventLoopGroup group = new NioEventLoopGroup();
    Bootstrap bootstrap = new Bootstrap();
    Channel channel;

    public void init() throws InterruptedException {
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new DecoderHandler());
                        pipeline.addLast(new EncoderHandler());
                        pipeline.addLast(new NameServerClientHandler());
                    }
                });

        ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 8020).sync();
        channel = channelFuture.channel();
    }

    public Channel getChannel() {
        if (this.channel == null) {
            throw new RuntimeException("channel is null");
        }
        return this.channel;
    }
}
