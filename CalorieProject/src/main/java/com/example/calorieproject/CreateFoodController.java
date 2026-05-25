package com.example.calorieproject;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.io.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.event.ActionEvent;

public class CreateFoodController {

    @FXML
    private TextField nameField;
    @FXML
    private TextField calField;
    @FXML
    private TextField fatField;
    @FXML
    private TextField carbField;
    @FXML
    private TextField proteinField;

    @FXML
    public void goBack(ActionEvent event) throws IOException {
        CalorieApplication.setRoot("CalorieTracker.fxml");
    }

    @FXML
    public void saveFood() {
        if (LoginController.loggedInUser == null) {
            System.out.println("No user logged in");
            return;
        }
        try {
            FoodItem f = new FoodItem(
                    nameField.getText().trim(),
                    Integer.parseInt(calField.getText().trim()),
                    Integer.parseInt(fatField.getText().trim()),
                    Integer.parseInt(carbField.getText().trim()),
                    Integer.parseInt(proteinField.getText().trim())
            );

            String user = LoginController.loggedInUser.getUsername();

            try (PrintWriter out = new PrintWriter(
                    new FileWriter(StoragePaths.FOOD, true))) {

                out.println(
                        user + "," +
                                f.getName() + "," +
                                f.getCalories() + "," +
                                f.getFat() + "," +
                                f.getCarbs() + "," +
                                f.getProtein()
                );
            }

            System.out.println("Saved food for user: " + user);

            nameField.clear();
            calField.clear();
            fatField.clear();
            carbField.clear();
            proteinField.clear();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}