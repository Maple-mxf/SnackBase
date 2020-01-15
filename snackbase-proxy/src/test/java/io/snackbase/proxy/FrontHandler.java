package io.snackbase.proxy;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

/**
 * @author maxuefeng
 * @since 2019/12/10
 */
public class FrontHandler extends ChannelInitializer<SocketChannel> {

    /**
     * 初始化管道时执行
     *
     * @param ch
     * @throws Exception
     */
        protected void initChannel(SocketChannel ch) throws Exception {
            // ch.pipeline().addLast()
            // ch.pipeline()

        ch.pipeline().addLast(new MySQLPacketDecoder());
    }


}
