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

    public static Account loggedInUser;

    @FXML
    public void handleLogin(ActionEvent event) {
        String user = usernameField.getText().trim();
        String pass = passwordField.getText().trim();

        if (user.isEmpty() || pass.isEmpty()) {
            statusLabel.setStyle("-fx-text-fill: red;");
            statusLabel.setText("Please enter both username and password.");
            return;
        }

        boolean authenticated = false;
        File file = new File(System.getProperty("user.home"), "accounts.txt");

        if (!file.exists()) {
            statusLabel.setStyle("-fx-text-fill: red;");
            statusLabel.setText("No accounts found. Please register.");
            return;
        }

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                Account acc = Account.fromCsvString(line);
                if (acc != null && acc.getUsername().equals(user) && acc.getPassword().equals(pass)) {
                    authenticated = true;
                    loggedInUser = acc;
                    break;
                }
            }
        } catch (FileNotFoundException e) {
            statusLabel.setStyle("-fx-text-fill: red;");
            statusLabel.setText("Error accessing account database.");
            return;
        }

        if (authenticated) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("CalorieTracker.fxml"));
                Parent root = loader.load();
                
                Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Calorie Tracker Dashboard");
                stage.show();
            } catch (IOException e) {
                statusLabel.setStyle("-fx-text-fill: red;");
                statusLabel.setText("Failed to load Dashboard UI.");
                e.printStackTrace();
            }
        } else {
            statusLabel.setStyle("-fx-text-fill: red;");
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
