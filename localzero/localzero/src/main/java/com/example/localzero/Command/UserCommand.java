package com.example.localzero.Command;

import com.example.localzero.DTO.InitiativeDTO;

import java.util.List;

public interface UserCommand {
    boolean executeAction();
    List<InitiativeDTO> fetchInitiatives();
    InitiativeDTO fetchInitiativeByID();

    default int fetchUserIdByEmail() {
        return -1;
    }
    
    default boolean checkJoinStatus() {
        return false;
    }
    
}
