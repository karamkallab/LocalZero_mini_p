package com.example.localzero.Controller;

import com.example.localzero.DTO.InitiativeDTO;
import com.example.localzero.chat.ChatMessage;
import com.example.localzero.chat.NotificationInitiatives;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    private final SimpMessageSendingOperations messagingTemplate;

    @Autowired
    public NotificationService(SimpMessageSendingOperations messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendInitiativeNotification(@Payload NotificationInitiatives chatMessage) {
        messagingTemplate.convertAndSend("/topic/initiative-notifications", chatMessage);
    }

}
