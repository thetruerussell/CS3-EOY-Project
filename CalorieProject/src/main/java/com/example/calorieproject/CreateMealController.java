package com.example.calorieproject;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.event.ActionEvent;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CreateMealController {

    @FXML private ComboBox<String> foodDropdown;
    @FXML private TextField qtyField;
    @FXML private TextField mealNameField;
    @FXML private ListView<String> mealList;
    @FXML private Label statusLabel;

    private int runningCalories = 0;
    private int runningFat = 0;
    private int runningCarbs = 0;
    private int runningProtein = 0;

    private List<FoodItem> availableFoods = new ArrayList<>();

    @FXML
    public void initialize() {
        if (LoginController.loggedInUser == null) {
            statusLabel.setText("No user logged in.");
            return;
        }
        foodDropdown.getItems().clear();
        availableFoods.clear();

        if (LoginController.loggedInUser == null) {
            statusLabel.setText("No user logged in.");
            return;
        }

        File file = new File(StoragePaths.FOOD);

        System.out.println("FOOD FILE PATH: " + file.getAbsolutePath());

        if (!file.exists()) {
            statusLabel.setText("Food file does not exist.");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            String line;
            int count = 0;

            while ((line = br.readLine()) != null) {

                String[] p = line.split(",");

                if (p.length == 6) {

                    System.out.println("READ FOOD LINE: " + line);

                    if (p[0].equals(LoginController.loggedInUser.getUsername())) {

                        FoodItem f = new FoodItem(
                                p[1],
                                Integer.parseInt(p[2]),
                                Integer.parseInt(p[3]),
                                Integer.parseInt(p[4]),
                                Integer.parseInt(p[5])
                        );

                        availableFoods.add(f);
                        foodDropdown.getItems().add(f.getName());
                        count++;
                    }
                }
            }

            statusLabel.setText("Loaded foods: " + count);

        } catch (Exception e) {
            statusLabel.setText("Error loading foods");
            e.printStackTrace();
        }
        if (foodDropdown.getItems().isEmpty()) {
            foodDropdown.setPromptText("No foods found");
        }
        System.out.println("Logged in user: " + LoginController.loggedInUser);
    }

    @FXML
    public void addFood() {

        try {

            String selectedName = foodDropdown.getValue();

            if (selectedName == null) {
                statusLabel.setText("Select a food.");
                return;
            }

            if (qtyField.getText().isEmpty()) {
                statusLabel.setText("Enter quantity.");
                return;
            }

            String q = qtyField.getText().trim();

            if (q.isEmpty()) {
                statusLabel.setText("Enter quantity.");
                return;
            }

            int qty = Integer.parseInt(q);

            FoodItem found = null;

            for (FoodItem f : availableFoods) {
                if (f.getName().equals(selectedName)) {
                    found = f;
                    break;
                }
            }

            if (found == null) {
                statusLabel.setText("Food not found.");
                return;
            }

            int cal = found.getCalories() * qty;
            int fat = found.getFat() * qty;
            int carb = found.getCarbs() * qty;
            int protein = found.getProtein() * qty;

            runningCalories += cal;
            runningFat += fat;
            runningCarbs += carb;
            runningProtein += protein;

            mealList.getItems().add(
                    qty + "x " + found.getName()
                            + " | " + cal + " cal"
            );

            statusLabel.setText("Added to meal.");

            qtyField.clear();

        } catch (NumberFormatException e) {
            statusLabel.setText("Quantity must be a number.");
        }
    }

    @FXML
    public void saveMeal(ActionEvent event) {

        try {

            String name = mealNameField.getText().trim();

            if (name.isEmpty()) {
                statusLabel.setText("Enter meal name.");
                return;
            }

            if (mealList.getItems().isEmpty()) {
                statusLabel.setText("Add foods before saving.");
                return;
            }

            Meal meal = new Meal(
                    name,
                    runningCalories,
                    runningFat,
                    runningCarbs,
                    runningProtein
            );

            String user = LoginController.loggedInUser.getUsername();
            try (PrintWriter out = new PrintWriter(
                    new FileWriter(StoragePaths.MEALS, true))) {

                out.println(
                        user + "," +
                                meal.getName() + "," +
                                meal.getCalories() + "," +
                                meal.getFat() + "," +
                                meal.getCarbs() + "," +
                                meal.getProtein()
                );
            }

            statusLabel.setText("Meal saved!");

            mealList.getItems().clear();
            mealNameField.clear();
            qtyField.clear();

            runningCalories = 0;
            runningFat = 0;
            runningCarbs = 0;
            runningProtein = 0;

        } catch (Exception e) {

            statusLabel.setText("Error saving meal.");
            e.printStackTrace();
        }
    }
    @FXML
    public void goBack(ActionEvent event) throws IOException {
        CalorieApplication.setRoot("CalorieTracker.fxml");
    }
}