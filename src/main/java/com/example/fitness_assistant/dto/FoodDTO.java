package com.example.fitness_assistant.dto;

public class FoodDTO {
    private String foodName;
    private String brand;
    private double calories;
    private double proteins; // белки
    private double fats;     // жиры
    private double carbs;    // углеводы

    public FoodDTO(String name, String brand, double calories, double proteins, double fats, double carbs) {
        this.foodName = name;
        this.brand = brand;
        this.calories = calories;
        this.proteins = proteins;
        this.fats = fats;
        this.carbs = carbs;
    }

    public String getFoodName() { return foodName; }
    public String getBrand() { return brand; }
    public double getCalories() { return calories; }
    public double getProteins() { return proteins; }
    public double getFats() { return fats; }
    public double getCarbs() { return carbs; }

}
