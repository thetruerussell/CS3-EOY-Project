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
import java.util.Scanner;

public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label statusLabel;

    @FXML
    public void handleLogin(ActionEvent event) {
        String user = usernameField.getText();
        String pass = passwordField.getText();

        if (user.isEmpty() || pass.isEmpty()) {
            statusLabel.setText("Please enter both username and password.");
            return;
        }

        boolean authenticated = false;
        try (Scanner scanner = new Scanner(new File("accounts.txt"))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    String storedUser = parts[1];
                    String storedPass = parts[2];
                    if (storedUser.equals(user) && storedPass.equals(pass)) {
                        authenticated = true;
                        break;
                    }
                }
            }
        } catch (FileNotFoundException e) {
            statusLabel.setText("No accounts found. Please register.");
            return;
        }

        if (authenticated) {
            statusLabel.setStyle("-fx-text-fill: green;");
            statusLabel.setText("Login successful!");

        } else {
            statusLabel.setText("Invalid username or password.");
        }
    }

    @FXML
    public void switchToRegister(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Register.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}

