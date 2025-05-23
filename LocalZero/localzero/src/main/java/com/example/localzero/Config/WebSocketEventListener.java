package com.example.localzero.Config;

import com.example.localzero.Observer.ChatBroadcaster;
import com.example.localzero.chat.ChatMessage;
import com.example.localzero.chat.MessageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;


@Component
@Slf4j
@RequiredArgsConstructor
public class WebSocketEventListener {

    @Autowired
    private final ChatBroadcaster chatBroadcaster;
    private final SimpMessageSendingOperations messagingTemplate;

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = (String) headerAccessor.getSessionAttributes().get("username");
        Principal user = (Principal) event.getUser();
        /*if (username != null) {
            chatBroadcaster.unregister(user.getName());
            System.out.println("Unregistered user: " + user.getName());
        }*/
    }

}