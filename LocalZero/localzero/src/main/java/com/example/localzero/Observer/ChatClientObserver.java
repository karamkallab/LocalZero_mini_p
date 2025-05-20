package com.example.localzero.Observer;

import com.example.localzero.chat.ChatMessage;

public interface ChatClientObserver {
    void receiveMessage(ChatMessage message);
}
