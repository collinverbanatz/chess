package server.websockett;


import com.google.gson.Gson;
import com.mysql.cj.protocol.x.XMessage;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();
    Gson gson = new Gson();


    public void add(String visitorName, Integer gameId, Session session) {
        var connection = new Connection(visitorName, gameId, session);
        connections.put(visitorName, connection);
    }

    public void remove(String visitorName) {
        connections.remove(visitorName);
    }

    public void broadcast(String excludeVisitorName, Integer gameId, ServerMessage notification) throws IOException {
        System.out.println("Broadcasting message: " + gson.toJson(notification));
        var removeList = new ArrayList<Connection>();
        for (var c : connections.values()) {
            if (c.session.isOpen()) {
                if (!c.visitorName.equals(excludeVisitorName) && Objects.equals(c.gameId, gameId)) {
                    c.send(gson.toJson(notification));
                }
            } else {
                removeList.add(c);
            }
        }

        // Clean up any connections that were left open.
        for (var c : removeList) {
            connections.remove(c.visitorName);
        }
    }
}
