package com.example.localzero.Config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketUserLogger {

    private final SimpUserRegistry simpUserRegistry;

    // This will log connected users every 10 seconds
    @Scheduled(fixedRate = 10000)
    public void logConnectedUsers() {
        log.info("Connected WebSocket Users:");
        for (SimpUser user : simpUserRegistry.getUsers()) {
            log.info(" - " + user.getName());
        }
    }
}
