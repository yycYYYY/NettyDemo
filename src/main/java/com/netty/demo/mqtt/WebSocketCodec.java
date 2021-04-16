package com.netty.demo.mqtt;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;

import java.util.List;

/**
 * WebSocketFrame convert to Bytebuf
 */
public class WebSocketCodec extends MessageToMessageCodec<BinaryWebSocketFrame, ByteBuf> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        BinaryWebSocketFrame result = new BinaryWebSocketFrame();
        //这里的Bytebuf需不需要也retain一下，加一下计数
        result.content().writeBytes(byteBuf);
        list.add(result);
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, BinaryWebSocketFrame binaryWebSocketFrame, List<Object> list) throws Exception {
        ByteBuf buf = binaryWebSocketFrame.content();
        buf.retain();
        list.add(buf);
    }

}
