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
import java.util.Objects;
import java.util.Random;

public class RegisterController {

    @FXML private TextField nameField;
    @FXML private TextField userField;
    @FXML private PasswordField passField;
    @FXML private PasswordField confirmPassField;
    @FXML private Label statusLabel;

    @FXML
    public void handleCreateAccount(ActionEvent event) {

        try {
            String name = nameField.getText().trim();
            String user = userField.getText().trim();

            String pass = passField.getText();
            String confirm = confirmPassField.getText();

            if (name.isEmpty() || user.isEmpty() || pass.isEmpty()) {
                statusLabel.setText("All fields are required.");
                return;
            }

            if (!pass.equals(confirm)) {
                statusLabel.setText("Passwords do not match.");
                return;
            }

            String hashed = SecurityUtil.hash(pass);

            Random r = new Random();
            int accNum = r.nextInt(900000) + 100000;

            Account acc = new Account(user, hashed, accNum, name);

            File file = new File(StoragePaths.ACCOUNTS);
            file.getParentFile().mkdirs();

            try (PrintWriter out = new PrintWriter(new FileWriter(file, true))) {
                out.println(acc.toCsvString());
            }

            switchToLogin(event);

        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Error creating account");
        }
    }

    @FXML
    public void switchToLogin(ActionEvent event) throws IOException {
        CalorieApplication.setRoot("Login.fxml");
    }
}