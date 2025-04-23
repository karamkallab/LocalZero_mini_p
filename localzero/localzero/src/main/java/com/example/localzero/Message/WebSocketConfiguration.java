package com.example.localzero.Message;

import java.net.http.WebSocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/*
 * This class is used to configure the WebSocket for the application.
 * WebSocketMessageBrokerConfigurer is an interface that allows configuring the WebSocket.
 * This is needed to activate WebSocket in spring and tell the client how to connect to the server.
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {

    /*
     * This method configures how messages are routed in the application.
     * "enableSimpleBroker" activates an in memory server that will be used to send messages to the client.
     * "setApplicationDestinationPrefixes" tells spring boot to send all incoming messages to "/app/", which
     * will be managed by @MessageMapping methods.
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

    /*
     * This method tells what endpoint the client should connect to.
     * The endpoint is "/chat" and it will be used to connect to the server.
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chat").setAllowedOrigins("*").withSockJS();
    }
}
