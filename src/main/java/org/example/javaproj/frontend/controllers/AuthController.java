package org.example.javaproj.frontend.controllers;

import org.example.javaproj.frontend.network.Client;
import org.example.javaproj.frontend.network.MockClient;
import org.example.javaproj.frontend.network.RealClient;
import org.example.javaproj.frontend.utils.Config;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AuthController {

    @FXML
    private TextField nameField;

    private Client client;

    @FXML
    private void handleEnter(ActionEvent event) {
        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            showAlert("Validation Error", "Name cannot be empty.");
            return;
        }

        if (Config.USE_REAL_CLIENT) {
            client = new RealClient(Config.SERVER_URL);
        } else {
            client = new MockClient();
        }

        boolean connected = client.connect();

        if (!connected) {
            showAlert("Connection Error", "Unable to connect to the server.");
            return;
        }

        client.sendMessage("JOIN:" + name);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/javaproj/frontend/main.fxml"));
            Scene scene = new Scene(loader.load());
            MainController mainController = loader.getController();
            mainController.initializeData(client, name);
            Stage stage = (Stage) nameField.getScene().getWindow();
            stage.setTitle("Collaborative Whiteboard - " + name);
            stage.setScene(scene);
            stage.setResizable(true);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load the main window.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
