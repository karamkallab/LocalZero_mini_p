package com.example.localzero.Controller;

import com.example.localzero.DTO.NotificationDTO;
import org.springframework.beans.factory.annotation.Autowired;
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

    public void sendInitiativeNotification(@Payload NotificationDTO chatMessage) {
        messagingTemplate.convertAndSend("/topic/initiative-notifications", chatMessage);
    }

}
