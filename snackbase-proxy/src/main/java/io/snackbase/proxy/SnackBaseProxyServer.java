package io.snackbase.proxy;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.snackbase.protocol.common.net.codec.MySqlPacketDecoder;
import io.snackbase.proxy.handler.FrontAuthChannelHandler;
import io.snackbase.proxy.handler.TailCommandHandler;

/**
 * @author maxuefeng
 * @see java.io.FileDescriptor
 * @see ChannelHandler
 * @since 2019/12/10
 * <p>
 * 在使用Handler的过程中，需要注意：
 * <p>
 * 　　1、ChannelInboundHandler之间的传递，通过调用 ctx.fireChannelRead(msg) 实现；调用ctx.write(msg) 将传递到ChannelOutboundHandler
 * 　　2、ctx.write()方法执行后，需要调用flush()方法才能令它立即执行。
 * 　　3、ChannelOutboundHandler {@link io.netty.channel.ChannelOutboundHandlerAdapter}在注册的时候需要放在最后一个ChannelInboundHandler之前，
 * 否则将无法传递到ChannelOutboundHandler
 * 　（流水线pipeline中outhander不能放到最后，否则不生效）
 * <p>
 * 　　4、Handler的消费处理放在最后一个处理。
 */
public class SnackBaseProxyServer {

    // private static Logger logger = LoggerFactory.getLogger(SnackBaseProxyServer.class);

    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    protected void initChannel(SocketChannel ch) {
                        ch.pipeline().addLast(new MySqlPacketDecoder());
                        ch.pipeline().addLast(new FrontAuthChannelHandler());
                        ch.pipeline().addLast(new TailCommandHandler());
                    }
                });

        ChannelFuture future = bootstrap.bind(3451).sync();

        future.channel().closeFuture().sync();
    }
}
