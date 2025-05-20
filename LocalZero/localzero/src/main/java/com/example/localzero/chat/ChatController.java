package com.example.localzero.chat;

import com.example.localzero.Controller.DatabaseController;
import com.example.localzero.DTO.InitiativeDTO;
import com.example.localzero.Observer.ChatBroadcaster;
import com.example.localzero.Observer.WebSocketChatClient;
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
    private final ChatBroadcaster chatBroadcaster;

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload ChatMessage chatMessage) {
        try {
            int fromUserId = databaseController.fetchIDByName(chatMessage.getSender());
            int toUserId = databaseController.fetchIDByName(chatMessage.getRecipient());
            databaseController.saveMessage(fromUserId, toUserId, chatMessage.getContent());

            chatBroadcaster.register(chatMessage.getSender(),
                    new WebSocketChatClient(chatMessage.getSender(), messagingTemplate));

            chatBroadcaster.register(chatMessage.getRecipient(),
                    new WebSocketChatClient(chatMessage.getRecipient(), messagingTemplate));

            chatBroadcaster.sendToBoth(chatMessage.getSender(), chatMessage.getRecipient(), chatMessage);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @MessageMapping("/chat.addUser")
    public void addUser(@Payload ChatMessage chatMessage,
                               SimpMessageHeaderAccessor headerAccessor,
                               Principal principal) {
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        chatBroadcaster.register(chatMessage.getSender(), new WebSocketChatClient(chatMessage.getSender(), messagingTemplate));
    }
}