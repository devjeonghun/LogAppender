package com.lj.log.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public class IbeLog {
    private String csid;
    private String message;
}
