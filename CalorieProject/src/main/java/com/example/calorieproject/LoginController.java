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
import java.util.Scanner;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label statusLabel;

    public static Account loggedInUser;

    @FXML
    public void handleLogin(ActionEvent event) {

        String user = usernameField.getText().trim();
        String pass = passwordField.getText();

        File file = new File(StoragePaths.ACCOUNTS);

        if (!file.exists()) {
            statusLabel.setText("No accounts found.");
            return;
        }

        try (Scanner sc = new Scanner(file)) {

            while (sc.hasNextLine()) {

                Account acc = Account.fromCsvString(sc.nextLine());

                if (acc != null &&
                        acc.getUsername().equals(user) &&
                        acc.checkPassword(pass)) {

                    loggedInUser = acc;

                    CalorieApplication.setRoot("CalorieTracker.fxml");
                    return;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        statusLabel.setText("Invalid login");
    }

    @FXML
    public void switchToRegister(ActionEvent event) throws IOException {
        CalorieApplication.setRoot("Register.fxml");
    }
}