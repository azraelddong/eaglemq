package com.infoepoch.cmgs.core;

import com.infoepoch.cmgs.coder.DecoderHandler;
import com.infoepoch.cmgs.coder.EncoderHandler;
import com.infoepoch.cmgs.event.EventBus;
import com.infoepoch.cmgs.handler.TcpNettyServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NameServerStarter {

    private int port;

    public NameServerStarter(int port) {
        this.port = port;
    }

    /**
     * Netty server启动
     */
    public void start() throws InterruptedException {
        NioEventLoopGroup boosGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(boosGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {

                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new DecoderHandler());
                        ch.pipeline().addLast(new EncoderHandler());
                        EventBus eventBus = new EventBus();
                        eventBus.init();
                        ch.pipeline().addLast(new TcpNettyServerHandler(eventBus));
                    }
                });

        // 监听jvm虚拟机关闭回调
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            boosGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }));
        ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
        channelFuture.channel().closeFuture().sync();
    }
}
