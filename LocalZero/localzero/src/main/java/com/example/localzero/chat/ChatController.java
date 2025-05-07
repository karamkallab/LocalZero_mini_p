package com.example.localzero.chat;

import com.example.localzero.Controller.DatabaseController;
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
        // Save message to DB
        try {
            int fromUserId = databaseController.fetchIDbyName(chatMessage.getSender());
            int toUserId = databaseController.fetchIDbyName(chatMessage.getRecipient());
            databaseController.saveMessage(fromUserId, toUserId, chatMessage.getContent());
        } catch (Exception e) {
            e.printStackTrace(); // Log error (or handle more gracefully)
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
    //@SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor, Principal principal) {
        // Add username in web socket session
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        System.out.println("Assigned principal: " + (principal != null ? principal.getName() : "null"));
        return chatMessage;
    }

    @GetMapping("/messages/history")
    public List<String> getMessageHistory(
            @RequestParam int fromUserId,
            @RequestParam int toUserId
    ) {
        return databaseController.fetchMessagesBetweenUsers(fromUserId, toUserId);
    }

    @GetMapping("/api/userId")
    public int getUserIdByName(@RequestParam String name) {
        return databaseController.fetchIDbyName(name);
    }
}