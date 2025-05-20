package com.example.localzero.Observer;

import com.example.localzero.chat.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

@RequiredArgsConstructor
public class WebSocketChatClient implements ChatClientObserver {

    private final String username;
    private final SimpMessageSendingOperations messagingTemplate;

    @Override
    public void receiveMessage(ChatMessage message) {
        messagingTemplate.convertAndSend("/user/" + username + "/queue/messages", message);
    }
}
