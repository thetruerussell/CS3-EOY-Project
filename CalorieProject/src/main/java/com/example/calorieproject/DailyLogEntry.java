package com.example.calorieproject;

public class DailyLogEntry {
    String timestamp;
    String type;
    String name;
    int calories;
    int protein;
    int carbs;
    int fat;

    @Override
    public String toString() {
        return type + ": " + name + " | " + calories;
    }
}