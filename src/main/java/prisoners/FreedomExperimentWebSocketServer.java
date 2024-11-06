package prisoners;

import static spark.Spark.externalStaticFileLocation;
import static spark.Spark.get;
import static spark.Spark.port;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import com.google.gson.Gson;

import spark.Spark;

public class FreedomExperimentWebSocketServer extends WebSocketServer {

    private Map<WebSocket, ExperimentSession> sessions = new HashMap<>();
    private Gson gson = new Gson();

    public FreedomExperimentWebSocketServer(int port) {
        super(new InetSocketAddress(port));
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        System.out.println("New connection: " + conn.getRemoteSocketAddress());
        sessions.put(conn, new ExperimentSession(conn));
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        sessions.remove(conn);
        System.out.println("Closed connection: " + conn.getRemoteSocketAddress());
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onMessage(WebSocket conn, String message) {
        Map<String, String> msg = gson.fromJson(message, HashMap.class);
        if ("start".equals(msg.get("action"))) {
            ExperimentSession session = sessions.get(conn);
            session.startExperiment();
        } else if ("stop".equals(msg.get("action"))) {
            ExperimentSession session = sessions.get(conn);
            session.stopExperiment();
        }
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        sessions.remove(conn);
        ex.printStackTrace();
    }

    @Override
    public void onStart() {
        System.out.println("Server started!");
    }

    public static void main(String[] args) {
        // Print the current working directory
        System.out.println("Current working directory: " + System.getProperty("user.dir"));
        port(8080);
        externalStaticFileLocation("/Users/brunoborges/work/100-prisoners/src/main/resources");
        get("/hello", (request, response) -> "Hello World!");

        System.out.println("Starting WebSocket server on port 8081");
        WebSocketServer server = new FreedomExperimentWebSocketServer(8081);
        server.start();

        // add hook to shutdown
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                server.stop();
                Spark.stop();
            } catch (InterruptedException e) {

            }
        }));
    }
}