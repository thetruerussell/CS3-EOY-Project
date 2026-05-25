package com.example.calorieproject;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Account {

    private String username;
    private String passwordHash;
    private int accountNumber;
    private String accountHolderName;

    private List<FoodItem> foodLog = new ArrayList<>();
    private List<Meal> mealLog = new ArrayList<>();

    private int calorieGoal = 2000;
    private int fatGoal = 70;
    private int carbGoal = 250;
    private int proteinGoal = 120;

    public Account(String username,
                   String passwordHash,
                   int accountNumber,
                   String name) {

        this.username = username;
        this.passwordHash = passwordHash;
        this.accountNumber = accountNumber;
        this.accountHolderName = name;

        loadGoals();
    }
    public boolean checkPassword(String rawPassword) {
        return passwordHash.equals(SecurityUtil.hash(rawPassword));
    }
    public void addFood(FoodItem f) {
        foodLog.add(f);
        persistFood(f);
    }

    public void loadFood(FoodItem f) {
        foodLog.add(f);
    }



    private void persistFood(FoodItem f) {

        try (PrintWriter out = new PrintWriter(
                new FileWriter(StoragePaths.FOOD, true))) {

            out.println(
                    username + "," +
                            f.getName() + "," +
                            f.getCalories() + "," +
                            f.getFat() + "," +
                            f.getCarbs() + "," +
                            f.getProtein()
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void addMeal(Meal m) {
        mealLog.add(m);
        persistMeal(m);
    }

    public void loadMeal(Meal m) {
        mealLog.add(m);
    }

    private void persistMeal(Meal m) {

        try (PrintWriter out = new PrintWriter(
                new FileWriter(StoragePaths.MEALS, true))) {

            out.println(
                    username + "," +
                            m.getName() + "," +
                            m.getCalories() + "," +
                            m.getFat() + "," +
                            m.getCarbs() + "," +
                            m.getProtein()
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public int getTotalCalories() {

        return foodLog.stream()
                .mapToInt(FoodItem::getCalories)
                .sum()

                +

                mealLog.stream()
                        .mapToInt(Meal::getCalories)
                        .sum();
    }

    public int getTotalProtein() {

        return foodLog.stream()
                .mapToInt(FoodItem::getProtein)
                .sum()

                +

                mealLog.stream()
                        .mapToInt(Meal::getProtein)
                        .sum();
    }

    public int getTotalCarbs() {

        return foodLog.stream()
                .mapToInt(FoodItem::getCarbs)
                .sum()

                +

                mealLog.stream()
                        .mapToInt(Meal::getCarbs)
                        .sum();
    }

    public int getTotalFat() {

        return foodLog.stream()
                .mapToInt(FoodItem::getFat)
                .sum()

                +

                mealLog.stream()
                        .mapToInt(Meal::getFat)
                        .sum();
    }

    public void setGoals(int calories,
                         int fat,
                         int carbs,
                         int protein) {

        this.calorieGoal = calories;
        this.fatGoal = fat;
        this.carbGoal = carbs;
        this.proteinGoal = protein;

        saveGoals();
    }

    private void saveGoals() {

        try {

            File input = new File(StoragePaths.GOALS);

            List<String> lines = new ArrayList<>();

            if (input.exists()) {

                BufferedReader br =
                        new BufferedReader(new FileReader(input));

                String line;

                while ((line = br.readLine()) != null) {

                    if (!line.startsWith(username + ",")) {
                        lines.add(line);
                    }
                }

                br.close();
            }

            lines.add(
                    username + "," +
                            calorieGoal + "," +
                            fatGoal + "," +
                            carbGoal + "," +
                            proteinGoal
            );

            PrintWriter out =
                    new PrintWriter(new FileWriter(input));

            for (String s : lines) {
                out.println(s);
            }

            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadGoals() {

        File file = new File(StoragePaths.GOALS);

        if (!file.exists()) return;

        try (BufferedReader br =
                     new BufferedReader(new FileReader(file))) {

            String line;

            while ((line = br.readLine()) != null) {

                String[] p = line.split(",");

                if (p[0].equals(username)) {

                    calorieGoal = Integer.parseInt(p[1]);
                    fatGoal = Integer.parseInt(p[2]);
                    carbGoal = Integer.parseInt(p[3]);
                    proteinGoal = Integer.parseInt(p[4]);

                    return;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getGoalFeedback() {

        StringBuilder msg = new StringBuilder();

        if (getTotalCalories() < calorieGoal * 0.8) {
            msg.append("⚠ Under calorie goal\n");
        }
        else if (getTotalCalories() <= calorieGoal) {
            msg.append("✅ Calories on track\n");
        }
        else {
            msg.append("🚨 Over calorie goal\n");
        }

        msg.append(feedback("Protein",
                getTotalProtein(),
                proteinGoal));

        msg.append(feedback("Carbs",
                getTotalCarbs(),
                carbGoal));

        msg.append(feedback("Fat",
                getTotalFat(),
                fatGoal));

        return msg.toString();
    }

    private String feedback(String name,
                            int value,
                            int goal) {

        if (value < goal * 0.8) {
            return "⚠ Low " + name + "\n";
        }
        else if (value <= goal) {
            return "✅ " + name + " good\n";
        }
        else {
            return "🚨 High " + name + "\n";
        }
    }
    public String toCsvString() {

        return username + "," +
                passwordHash + "," +
                accountNumber + "," +
                accountHolderName;
    }

    public static Account fromCsvString(String csvLine) {

        String[] p = csvLine.split(",");

        return new Account(
                p[0],
                p[1],
                Integer.parseInt(p[2]),
                p[3]
        );
    }
    public String getUsername() {
        return username;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public int getCalorieGoal() {
        return calorieGoal;
    }

    public int getProteinGoal() {
        return proteinGoal;
    }

    public int getCarbGoal() {
        return carbGoal;
    }

    public int getFatGoal() {
        return fatGoal;
    }
}