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
            String pass = passField.getText().trim();

            if (name.isEmpty() || user.isEmpty() || pass.isEmpty()) {
                statusLabel.setStyle("-fx-text-fill: red;");
                statusLabel.setText("All fields are required.");
                return;
            }
            if (!pass.equals(confirmPassField.getText())) {
                statusLabel.setStyle("-fx-text-fill: red;");
                statusLabel.setText("Passwords do not match.");
                return;
            }

            Random rand = new Random();
            int generatedAccNum = rand.nextInt(900000) + 100000;
            Account newAccount = new Account(user, pass, generatedAccNum, name);

            File file = new File(System.getProperty("user.home"), "accounts.txt");

            if (!file.exists()) {
                file.createNewFile();
            }

            try (FileWriter fw = new FileWriter(file, true);
                 BufferedWriter bw = new BufferedWriter(fw);
                 PrintWriter out = new PrintWriter(bw)) {

                out.println(newAccount.toCsvString());
                out.flush();
                System.out.println("💾 ACCOUNT WRITTEN TO DISK AT: " + file.getAbsolutePath());
            }

            switchToLogin(event);

        } catch (Exception e) {
            statusLabel.setStyle("-fx-text-fill: red;");
            statusLabel.setText("System error tracking registry setup.");
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
