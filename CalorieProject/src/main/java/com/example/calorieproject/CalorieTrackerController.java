package com.example.calorieproject;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CalorieTrackerController {

    @FXML private Label welcomeLabel;

    @FXML private Label calorieProgressLabel;
    @FXML private Label proteinProgressLabel;
    @FXML private Label carbProgressLabel;
    @FXML private Label fatProgressLabel;

    @FXML private Label feedbackLabel;

    @FXML private ListView<String> logListView;
    List<String> rawLogLines = new ArrayList<>();
    @FXML private TextField searchField;
    @FXML private ComboBox<String> searchResults;

    private List<FoodItem> foodInventory = new ArrayList<>();
    private List<Meal> mealInventory = new ArrayList<>();

    private int totalCalories = 0;
    private int totalProtein = 0;
    private int totalCarbs = 0;
    private int totalFat = 0;

    private boolean lock = false;
    @FXML
    public void handleEatSelected() {

        if (lock) return;
        lock = true;

        try {
            String selected = searchResults.getValue();
            if (selected == null) return;

            if (selected.startsWith("FOOD: ")) {

                String name = selected.replace("FOOD: ", "");

                for (FoodItem f : foodInventory) {
                    if (f.getName().equals(name)) {

                        writeDailyLog("FOOD", f.getName(),
                                f.getCalories(),
                                f.getProtein(),
                                f.getCarbs(),
                                f.getFat());

                        break;
                    }
                }
            }

            if (selected.startsWith("MEAL: ")) {

                String name = selected.replace("MEAL: ", "");

                for (Meal m : mealInventory) {
                    if (m.getName().equals(name)) {

                        writeDailyLog("MEAL", m.getName(),
                                m.getCalories(),
                                m.getProtein(),
                                m.getCarbs(),
                                m.getFat());

                        break;
                    }
                }
            }

            loadDailyLogs();
            recalcFromDailyLogs();
            refreshUI();

        } finally {
            lock = false;
        }
    }
    private void writeDailyLog(String type,
                               String name,
                               int cal,
                               int protein,
                               int carbs,
                               int fat) {

        try (BufferedWriter bw = new BufferedWriter(
                new FileWriter(StoragePaths.DAILY, true))) {

            String line =
                    LoginController.loggedInUser.getUsername() + "," +
                            type + "," +
                            name + "," +
                            cal + "," +
                            protein + "," +
                            carbs + "," +
                            fat + "," +
                            today();

            bw.write(line);
            bw.newLine();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void handleSearch() {

        searchResults.getItems().clear();

        String query = searchField.getText();
        if (query == null) query = "";

        query = query.toLowerCase().trim();

        for (FoodItem f : foodInventory) {
            if (f.getName().toLowerCase().contains(query)) {
                searchResults.getItems().add("FOOD: " + f.getName());
            }
        }

        for (Meal m : mealInventory) {
            if (m.getName().toLowerCase().contains(query)) {
                searchResults.getItems().add("MEAL: " + m.getName());
            }
        }
    }
    private void loadInventory() {

        foodInventory.clear();
        mealInventory.clear();

        try (BufferedReader br = new BufferedReader(new FileReader(StoragePaths.FOOD))) {

            String line;
            while ((line = br.readLine()) != null) {

                String[] p = line.split(",");

                if (p.length == 6 &&
                        p[0].equals(LoginController.loggedInUser.getUsername())) {

                    foodInventory.add(new FoodItem(
                            p[1],
                            Integer.parseInt(p[2]),
                            Integer.parseInt(p[3]),
                            Integer.parseInt(p[4]),
                            Integer.parseInt(p[5])
                    ));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        try (BufferedReader br = new BufferedReader(new FileReader(StoragePaths.MEALS))) {

            String line;
            while ((line = br.readLine()) != null) {

                String[] p = line.split(",");

                if (p.length == 6 &&
                        p[0].equals(LoginController.loggedInUser.getUsername())) {

                    mealInventory.add(new Meal(
                            p[1],
                            Integer.parseInt(p[2]),
                            Integer.parseInt(p[3]),
                            Integer.parseInt(p[4]),
                            Integer.parseInt(p[5])
                    ));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void loadDailyLogs() {

        logListView.getItems().clear();
        rawLogLines.clear();

        File file = new File(StoragePaths.DAILY);
        if (!file.exists()) return;

        String user = LoginController.loggedInUser.getUsername();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            String line;

            while ((line = br.readLine()) != null) {

                String[] p = line.split(",");
                if (p.length < 8) continue;

                if (!p[0].equals(user)) continue;
                if (!p[7].equals(today())) continue;

                String display = p[1] + ": " + p[2] + " | " + p[3] + " cal";

                rawLogLines.add(line);
                logListView.getItems().add(display);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void initialize() {

        Account user = LoginController.loggedInUser;
        if (user == null) return;

        welcomeLabel.setText("Welcome, " + user.getAccountHolderName());

        loadInventory();
        loadDailyLogs();
        recalcFromDailyLogs();
        refreshUI();

        logListView.setOnMouseClicked(e -> deleteSelectedLog());
    }
    private void recalcFromDailyLogs() {

        totalCalories = 0;
        totalProtein = 0;
        totalCarbs = 0;
        totalFat = 0;

        File file = new File(StoragePaths.DAILY);
        if (!file.exists()) return;

        String user = LoginController.loggedInUser.getUsername();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            String line;

            while ((line = br.readLine()) != null) {

                String[] p = line.split(",");
                if (p.length < 8) continue;

                if (!p[0].equals(user)) continue;
                if (!p[7].equals(today())) continue;

                totalCalories += Integer.parseInt(p[3]);
                totalProtein += Integer.parseInt(p[4]);
                totalCarbs += Integer.parseInt(p[5]);
                totalFat += Integer.parseInt(p[6]);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void deleteSelectedLog() {

        int index = logListView.getSelectionModel().getSelectedIndex();
        if (index < 0 || index >= rawLogLines.size()) return;

        String file = StoragePaths.DAILY;

        List<String> updated = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            String line;
            int currentIndex = 0;

            while ((line = br.readLine()) != null) {

                String[] p = line.split(",");
                if (p.length < 8) continue;

                String user = LoginController.loggedInUser.getUsername();

                boolean isTarget =
                        p[0].equals(user)
                                && p[7].equals(today())
                                && currentIndex == index;

                if (!isTarget) {
                    updated.add(line);
                }

                currentIndex++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, false))) {

            for (String l : updated) {
                bw.write(l);
                bw.newLine();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        loadDailyLogs();
        recalcFromDailyLogs();
        refreshUI();
    }
    private void refreshUI() {

        Account u = LoginController.loggedInUser;

        calorieProgressLabel.setText("Calories: " + totalCalories + " / " + u.getCalorieGoal());
        proteinProgressLabel.setText("Protein: " + totalProtein + "g / " + u.getProteinGoal() + "g");
        carbProgressLabel.setText("Carbs: " + totalCarbs + "g / " + u.getCarbGoal() + "g");
        fatProgressLabel.setText("Fat: " + totalFat + "g / " + u.getFatGoal() + "g");

        feedbackLabel.setText(u.getGoalFeedback());
    }

    private String today() {
        return LocalDate.now().toString();
    }
    @FXML public void openCreateFood(ActionEvent e) throws IOException {
        CalorieApplication.setRoot("CreateFood.fxml");
    }

    @FXML public void openCreateMeal(ActionEvent e) throws IOException {
        CalorieApplication.setRoot("CreateMeal.fxml");
    }

    @FXML public void openGoalEditor(ActionEvent e) throws IOException {
        CalorieApplication.setRoot("GoalEditor.fxml");
    }

    @FXML public void handleLogout(ActionEvent event) throws IOException {
        LoginController.loggedInUser = null;
        CalorieApplication.setRoot("Login.fxml");
    }
}