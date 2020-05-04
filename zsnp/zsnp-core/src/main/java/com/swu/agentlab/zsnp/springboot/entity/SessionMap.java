package com.swu.agentlab.zsnp.springboot.entity;

import lombok.Data;

import javax.websocket.Session;
import java.util.List;
import java.util.Map;

@Data
public class SessionMap {
    private Map<String,Session> sessionMap;
}
