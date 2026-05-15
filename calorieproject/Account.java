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

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public int getAccountNumber() { return accountNumber; }
    public String getAccountHolderName() { return accountHolderName; }
}
