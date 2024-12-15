package org.example.javaproj.frontend.network;

import javafx.application.Platform;
import org.example.javaproj.frontend.controllers.MainController;

public class MockClient implements Client {

    private MainController controller;

    @Override
    public boolean connect() {
        return true;
    }

    @Override
    public void sendMessage(String message) {
        Platform.runLater(() -> {
            if (controller != null && message.startsWith("DRAW:")) {
                String[] parts = message.substring(5).split(",");
                double x = Double.parseDouble(parts[0]);
                double y = Double.parseDouble(parts[1]);
                String color = parts[2];
                controller.drawLine(x, y, color);
            }
        });
    }

    @Override
    public void close() {
    }

    public void setController(MainController controller) {
        this.controller = controller;
    }
}
