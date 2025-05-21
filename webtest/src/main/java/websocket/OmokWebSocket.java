package websocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpSession;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/ws/omok/{roomId}", configurator = HttpSessionConfigurator.class)
public class OmokWebSocket {

    private static final Map<String, List<Session>> roomSessions = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, EndpointConfig config, @PathParam("roomId") String roomId) throws IOException {
        HttpSession httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        String userId = httpSession != null ? (String) httpSession.getAttribute("user_id") : null;

        System.out.println("[WebSocket] onOpen - roomId: " + roomId + ", userId: " + userId);

        if (userId == null) {
            System.out.println("[WebSocket] user_id is null, closing session");
            session.close();
            return;
        }

        roomSessions.putIfAbsent(roomId, new ArrayList<>());
        List<Session> sessions = roomSessions.get(roomId);
        sessions.add(session);

        int count = sessions.size();
        for (Session s : sessions) {
            s.getBasicRemote().sendText("{\"type\":\"waiting\", \"count\":" + count + "}");
        }

        if (count == 2) {
            for (Session s : sessions) {
                s.getBasicRemote().sendText("{\"type\":\"playing\"}");
            }
        }
    }

    @OnMessage
    public void onMessage(String message, Session session, @PathParam("roomId") String roomId) throws IOException {
        List<Session> sessions = roomSessions.get(roomId);
        if (sessions != null) {
            for (Session s : sessions) {
                s.getBasicRemote().sendText(message);
            }
        }
    }

    @OnClose
    public void onClose(Session session, @PathParam("roomId") String roomId) {
        List<Session> sessions = roomSessions.get(roomId);
        if (sessions != null) {
            sessions.remove(session);
            if (sessions.isEmpty()) {
                roomSessions.remove(roomId);
            }
        }
        System.out.println("[WebSocket] onClose - roomId: " + roomId);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.err.println("[WebSocket] Error: " + throwable.getMessage());
    }
}
