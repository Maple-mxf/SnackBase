package io.snackbase.proxy.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.snackbase.protocol.common.net.codec.MySqlPacketDecoder;

/**
 * @author maxuefeng
 * @since 2019/12/14
 */
public class FrontChannelHandler extends ChannelInitializer<SocketChannel> {

    /**
     * @param ch
     */
    protected void initChannel(SocketChannel ch) {
        ch.pipeline().addLast(new MySqlPacketDecoder());
    }
}
