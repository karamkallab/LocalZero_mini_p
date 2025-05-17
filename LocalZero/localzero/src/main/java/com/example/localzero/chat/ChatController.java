package com.example.localzero.chat;

import com.example.localzero.Controller.DatabaseController;
import com.example.localzero.DTO.InitiativeDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessageSendingOperations messagingTemplate;
    private final DatabaseController databaseController;


    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload ChatMessage chatMessage) {
        try {
            int fromUserId = databaseController.fetchIDByName(chatMessage.getSender());
            int toUserId = databaseController.fetchIDByName(chatMessage.getRecipient());
            databaseController.saveMessage(fromUserId, toUserId, chatMessage.getContent());
            //Notify user
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Send to both sender and recipient
        messagingTemplate.convertAndSendToUser(
                chatMessage.getRecipient(),
                "/queue/messages",
                chatMessage
        );

        messagingTemplate.convertAndSendToUser(
                chatMessage.getSender(),
                "/queue/messages",
                chatMessage
        );
    }

    @MessageMapping("/chat.addUser")
    public ChatMessage addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor, Principal principal) {
        // Add username in web socket session
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        System.out.println("Assigned principal: " + (principal != null ? principal.getName() : "null"));
        return chatMessage;
    }

    @MessageMapping("/notis.initiative")
    public void sendInitiativeNoti(@Payload ChatMessage chatMessage) {
        messagingTemplate.convertAndSend("/queue/messages", chatMessage);
    }

}