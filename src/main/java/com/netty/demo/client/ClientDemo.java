package com.netty.demo.client;

import org.fusesource.mqtt.client.*;

import java.net.URISyntaxException;
import java.util.Arrays;

public class ClientDemo {
    public static void main(String[] args) throws Exception {
        MQTT mqtt = new MQTT();
        mqtt.setHost("ssl://qa-live.tigerfintech.com:8883");
        mqtt.setClientId("4123f74b6dc75336e2083da93785f31b6e0062e1");
        mqtt.setUserName("3551240843164253");
        mqtt.setPassword("cNKs5jtjjNxCfO8QN3txjarsMcmh3x");
        BlockingConnection connection = mqtt.blockingConnection();
        Topic[] topics = {new Topic("quote/MHImain/detail", QoS.AT_LEAST_ONCE)};
        byte[] qoses = connection.subscribe(topics);
        Message message = connection.receive();
        System.out.println(message.toString());
        System.out.println(Arrays.toString(message.getPayload()));
        System.out.println(message.getTopic());
        byte[] payload = message.getPayload();
        message.ack();
    }
}
