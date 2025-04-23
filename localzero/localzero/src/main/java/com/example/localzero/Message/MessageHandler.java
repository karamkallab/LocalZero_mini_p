package com.example.localzero.Message;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

/*
 * This class is responsible for handling messages between the client and server.
 * This class works as a "WebSocket Controller", like a RestController but for WebSocket.
 */
@Controller
public class MessageHandler {
    
    @MessageMapping("/send") //frontend sends messages to this endpoint
    @SendTo("/topic/messages") //each client that is listening to this endpoint will receive the message
    public String sendMessage(String message) {
        return message;
    }
}
