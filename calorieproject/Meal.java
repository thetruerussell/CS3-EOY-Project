package com.example.calorieproject;

public class Meal {
    private String name;
    private FoodItem[] foodItems;
    private int calories;

    public Meal(String name, int calories) {
        this.name = name;
        this.calories = calories;
        this.foodItems = new FoodItem[0];
    }

    public String getName() {
        return name;
    }

    public int getCalories() {
        return calories;
    }
}