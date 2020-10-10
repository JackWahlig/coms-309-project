package com.example.crossfire.websocket;

import java.util.HashMap;
import java.util.Map;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import org.json.*;
import org.springframework.stereotype.Component;

/**
 *
 * @author Zane Seuser
 *
 */
@ServerEndpoint("/websocket/{playerId}")
@Component
public class WebSocketServer {

    private static Map<Session, Integer> sessionMap = new HashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("playerId") Integer playerId) {
        sessionMap.put(session, playerId);
    }

    @OnMessage
    public void onMessage(String message) {
        broadcast(message);
    }

    @OnClose
    public void onClose(Session session) {
        sessionMap.remove(session);
    }

    private static void broadcast(String data) {
        JSONObject dataObject = new JSONObject(data);
        sessionMap.forEach((session, playerId) -> {
            if (playerId == dataObject.getInt("playerId")) {
                synchronized (session) {
                    try {
                        session.getBasicRemote().sendText(data);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }
}