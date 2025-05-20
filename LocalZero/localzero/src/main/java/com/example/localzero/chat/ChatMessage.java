package com.example.localzero.chat;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessage {

    private MessageType type;
    private String content;
    private String sender;
    private String recipient;
}