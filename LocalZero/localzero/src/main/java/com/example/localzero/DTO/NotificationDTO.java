package com.example.localzero.DTO;

import com.example.localzero.chat.MessageType;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationDTO {
    private MessageType type;
    private String content;
    private int sender;
}
