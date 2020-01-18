package io.snackbase.proxy.handler;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * 尾部的Handler {@link ChannelHandlerAdapter}
 *
 * @author maxuefeng
 * @since 2020/1/18
 */
public class TailCommandHandler extends ChannelHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);
        System.err.println(msg);
    }
}
