package com.example.calorieproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.*;

public class RegisterController {
    @FXML private TextField nameField;
    @FXML private TextField userField;
    @FXML private PasswordField passField;
    @FXML private PasswordField confirmPassField;
    @FXML private Label statusLabel;

    @FXML
    public void handleCreateAccount(ActionEvent event) {
        try {
            if (nameField == null || userField == null || passField == null) {
                System.out.println("Error: FXML IDs do not match Controller fields!");
                return;
            }

            String name = nameField.getText();
            String user = userField.getText();
            String pass = passField.getText();

            if (name.isEmpty() || user.isEmpty() || pass.isEmpty()) {
                statusLabel.setText("All fields are required.");
                return;
            }

            if (!pass.equals(confirmPassField.getText())) {
                statusLabel.setText("Passwords do not match.");
                return;
            }

            // Save to text file
            try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("accounts.txt", true)))) {
                out.println(name + "," + user + "," + pass);
                switchToLogin(event);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void switchToLogin(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
