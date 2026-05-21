package com.example.calorieproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;

public class CalorieTrackerController {
    @FXML private Label welcomeLabel;
    @FXML private Label totalCaloriesLabel;
    @FXML private TextField foodNameField;
    @FXML private TextField calorieField;
    @FXML private ListView<String> logListView;

    private int runningTotalCalories = 0;

    @FXML
    public void initialize() {
        if (LoginController.loggedInUser != null) {
            welcomeLabel.setText("Welcome, " + LoginController.loggedInUser.getAccountHolderName() + "!");
        } else {
            welcomeLabel.setText("Welcome, Guest User!");
        }
    }

    @FXML
    public void handleAddFood(ActionEvent event) {
        String name = foodNameField.getText().trim();
        String calText = calorieField.getText().trim();

        if (name.isEmpty() || calText.isEmpty()) return;

        try {
            int cals = Integer.parseInt(calText);
            FoodItem food = new FoodItem(name, cals);

            logListView.getItems().add(food.getName() + " - " + food.getCalories() + " kcal (Food)");
            updateCalorieTotal(food.getCalories());
            clearInputs();
        } catch (NumberFormatException e) {
            System.out.println("Invalid numeric input context encountered.");
        }
    }

    @FXML
    public void handleAddMeal(ActionEvent event) {
        String name = foodNameField.getText().trim();
        String calText = calorieField.getText().trim();

        if (name.isEmpty() || calText.isEmpty()) return;

        try {
            int cals = Integer.parseInt(calText);
            Meal meal = new Meal(name, cals);

            logListView.getItems().add(meal.getName() + " - " + meal.getCalories() + " kcal (Meal Package)");
            updateCalorieTotal(meal.getCalories());
            clearInputs();
        } catch (NumberFormatException e) {
            System.out.println("Invalid numeric input context encountered.");
        }
    }

    @FXML
    public void handleLogout(ActionEvent event) throws IOException {
        LoginController.loggedInUser = null;
        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Calorie Project Login");
        stage.show();
    }

    private void updateCalorieTotal(int amount) {
        runningTotalCalories += amount;
        totalCaloriesLabel.setText(String.valueOf(runningTotalCalories));
    }

    private void clearInputs() {
        foodNameField.clear();
        calorieField.clear();
    }
}
