package com.example.localzero.Observer;

import com.example.localzero.chat.ChatMessage;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class ChatBroadcaster {

    private final Map<String, ChatClientObserver> observers = new ConcurrentHashMap<>();

    public void register(String username, ChatClientObserver observer) {
        observers.put(username, observer); // Replaces old observer if user reconnects
    }

    public void unregister(String username) {
        observers.remove(username);
    }

    public void sendTo(String username, ChatMessage message) {
        ChatClientObserver observer = observers.get(username);
        if (observer != null) {
            observer.receiveMessage(message);
        }
    }

    public void sendToBoth(String user1, String user2, ChatMessage message) {
        sendTo(user1, message);
        sendTo(user2, message);
    }

    public void broadcast(ChatMessage message) {
        observers.values().forEach(observer -> observer.receiveMessage(message));
    }
}