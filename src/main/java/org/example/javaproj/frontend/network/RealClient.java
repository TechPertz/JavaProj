package org.example.javaproj.frontend.network;

import org.example.javaproj.frontend.controllers.MainController;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import java.net.URI;

public class RealClient extends WebSocketClient {

    private MainController controller;

    public RealClient(String serverUri) {
        super(URI.create(serverUri));
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
    }

    @Override
    public void onMessage(String message) {
        if (controller != null && message.startsWith("DRAW:")) {
            String[] parts = message.substring(5).split(",");
            double x = Double.parseDouble(parts[0]);
            double y = Double.parseDouble(parts[1]);
            String color = parts[2];
            controller.drawLine(x, y, color);
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
    }

    @Override
    public void onError(Exception ex) {
    }

    public void setController(MainController controller) {
        this.controller = controller;
    }
}
