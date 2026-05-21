package com.example.calorieproject;

import java.util.ArrayList;
import java.util.List;

public class Account {
    private String username;
    private String password;
    private int accountNumber;
    private String accountHolderName;
    private List<FoodItem> customFoodItem;
    private List<Meal> customMeal;

    public Account(String username, String password, int accountNumber, String accountHolderName) {
        this.username = username;
        this.password = password;
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.customFoodItem = new ArrayList<>();
        this.customMeal = new ArrayList<>();
    }

    public String toCsvString() {
        return username + "," + password + "," + accountNumber + "," + accountHolderName;
    }

    public static Account fromCsvString(String csvLine) {
        if (csvLine == null || csvLine.trim().isEmpty()) return null;

        String[] parts = csvLine.split(",");

        if (parts.length >= 4) {
            String user = parts[0].trim();
            String pass = parts[1].trim();
            int accNum = Integer.parseInt(parts[2].trim());
            String name = parts[3].trim();

            return new Account(user, pass, accNum, name);
        }
        return null;
    }


    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public int getAccountNumber() { return accountNumber; }
    public String getAccountHolderName() { return accountHolderName; }
}
