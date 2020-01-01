package io.snackbase.proxy;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.snackbase.protocol.common.config.SocketConfig;
import io.snackbase.protocol.common.config.SystemConfig;

/**
 * @author maxuefeng
 * @since 2019/12/10
 */
public class SnackBaseProxyServer {

    public static void main(String[] args) {
        // acceptor
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        // worker
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {

            // dataPool wrapper
            ServerBootstrap b = new ServerBootstrap();

            // 这边的childHandler是用来管理accept的
            // 由于线程间传递的是byte[],所以内存池okay
            // 只需要保证分配ByteBuf和write在同一个线程(函数)就行了
            b.group(bossGroup, workerGroup)

                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, SocketConfig.CONNECT_TIMEOUT_MILLIS)
                    .option(ChannelOption.SO_TIMEOUT, SocketConfig.SO_TIMEOUT)
                    // handler只能设置一个
                    .childHandler(new FrontHandler());

            ChannelFuture f = b.bind(SystemConfig.ServerPort).sync();
            f.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
