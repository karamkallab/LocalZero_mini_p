package com.example.localzero.chat;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationInitiatives {
    private MessageType type;
    private String content;
    private int sender;
}
