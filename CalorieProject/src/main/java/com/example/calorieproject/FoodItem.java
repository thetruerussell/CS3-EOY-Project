package com.example.calorieproject;

public class FoodItem {

    private String name;
    private int calories;
    private int fat;
    private int carbs;
    private int protein;

    public FoodItem(String name, int calories, int fat, int carbs, int protein) {
        this.name = name;
        this.calories = calories;
        this.fat = fat;
        this.carbs = carbs;
        this.protein = protein;
    }

    public String getName() { return name; }
    public int getCalories() { return calories; }
    public int getFat() { return fat; }
    public int getCarbs() { return carbs; }
    public int getProtein() { return protein; }
}