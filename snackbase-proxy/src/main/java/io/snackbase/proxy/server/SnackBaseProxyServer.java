package io.snackbase.proxy.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author maxuefeng
 * @see ChannelHandler
 * @since 2019/12/10
 */
public class SnackBaseProxyServer {

    private static Logger logger = LoggerFactory.getLogger(SnackBaseProxyServer.class);

    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap bootstrap= new ServerBootstrap();
        bootstrap.group(bossGroup,workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new FrontChannelHandler());

        ChannelFuture future = bootstrap.bind(3451).sync();

        future.channel().closeFuture().sync();
    }
}
