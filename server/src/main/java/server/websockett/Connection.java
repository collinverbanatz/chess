package server.websockett;

import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;

public class Connection {
    public String visitorName;
    public Session session;
    public Integer gameId;

    public Connection(String visitorName, Integer gameId, Session session) {
        this.visitorName = visitorName;
        this.gameId = gameId;
        this.session = session;
    }

    public void send(String msg) throws IOException {
        session.getRemote().sendString(msg);
    }
}