package org.example.javaproj.frontend.controllers;

import org.example.javaproj.frontend.network.Client;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ListView;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class MainController {

    @FXML
    private Canvas whiteboardCanvas;

    @FXML
    private ListView<String> usersListView;

    @FXML
    private ToggleButton penButton;

    @FXML
    private ToggleButton eraserButton;

    private GraphicsContext gc;
    private Color currentColor = Color.BLACK;
    private Client client;
    private String username;

    public void initializeData(Client client, String username) {
        this.client = client;
        this.username = username;
        gc = whiteboardCanvas.getGraphicsContext2D();
        whiteboardCanvas.addEventHandler(MouseEvent.MOUSE_PRESSED, this::onMousePressed);
        whiteboardCanvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, this::onMouseDragged);
        whiteboardCanvas.addEventHandler(MouseEvent.MOUSE_RELEASED, this::onMouseReleased);
    }

    private void onMousePressed(MouseEvent event) {
        gc.beginPath();
        gc.moveTo(event.getX(), event.getY());
        gc.stroke();
    }

    private void onMouseDragged(MouseEvent event) {
        gc.lineTo(event.getX(), event.getY());
        gc.stroke();
        client.sendMessage("DRAW:" + event.getX() + "," + event.getY() + "," + toHex(currentColor));
    }

    private void onMouseReleased(MouseEvent event) {
        gc.closePath();
    }

    private String toHex(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }

    @FXML
    private void selectPen() {
        currentColor = Color.BLACK;
    }

    @FXML
    private void selectEraser() {
        currentColor = Color.WHITE;
    }

    public void addUser(String user) {
        usersListView.getItems().add(user);
    }

    public void removeUser(String user) {
        usersListView.getItems().remove(user);
    }

    public void drawLine(double x, double y, String colorHex) {
        Color color = Color.web(colorHex);
        gc.setStroke(color);
        gc.lineTo(x, y);
        gc.stroke();
    }
}
