package com.example.localzero.Controller;

import com.example.localzero.DTO.InitiativeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    private final SimpMessageSendingOperations messagingTemplate;

    @Autowired
    public NotificationService(SimpMessageSendingOperations messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping ("/notificationInitiatives")
    public void broadcastInitiative(InitiativeDTO initiative) {
        System.out.println("🔔 Broadcasting initiative to /topic/initiatives");
        messagingTemplate.convertAndSend("/topic/initiatives", initiative);
    }
}
