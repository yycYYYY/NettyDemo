package com.netty.demo.heartbeat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.mqtt.*;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;


public class MqttBrokerHandler extends ChannelInboundHandlerAdapter {

    public static final MqttBrokerHandler INSTANCE = new MqttBrokerHandler();

//    private MqttBrokerHandler(){}

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        MqttMessage mqttMessage = (MqttMessage) msg;
        System.out.println("Received MQTT message: " + mqttMessage);
        switch (mqttMessage.fixedHeader().messageType()) {
            case CONNECT:
                MqttFixedHeader connackFixedHeader =
                        new MqttFixedHeader(MqttMessageType.CONNACK, false, MqttQoS.AT_MOST_ONCE, false, 0);
                MqttConnAckVariableHeader mqttConnAckVariableHeader =
                        new MqttConnAckVariableHeader(MqttConnectReturnCode.CONNECTION_ACCEPTED, false);
                MqttConnAckMessage connack = new MqttConnAckMessage(connackFixedHeader, mqttConnAckVariableHeader);
                System.out.println("SENT ACK");
                ctx.writeAndFlush(connack);
                break;
            case PINGREQ:
                MqttFixedHeader pingreqFixedHeader = new MqttFixedHeader(MqttMessageType.PINGRESP, false,
                        MqttQoS.AT_MOST_ONCE, false, 0);
                MqttMessage pingResp = new MqttMessage(pingreqFixedHeader);
                System.out.println("SENT RES");
                ctx.writeAndFlush(pingResp);
                break;
            case DISCONNECT:
                ctx.close();
                System.out.println("CLOSE CONNECT");
                break;
            default:
                System.out.println("Unexpected message type: " + mqttMessage.fixedHeader().messageType());
                ReferenceCountUtil.release(msg);
                ctx.close();
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        System.out.println("Channel heartBeat lost");
        if (evt instanceof IdleStateEvent && IdleState.READER_IDLE == ((IdleStateEvent) evt).state()) {
            ctx.close();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
