package com.example.healthyplus.Model;

public class Dish {
    private String id;
    private String name;
    private String calories;
    private String carb;
    private String protein;
    private String fat;
    private String recipe;

    public Dish() {
    }

    public Dish(String id, String name, String calories, String carb, String protein, String fat, String recipe) {
        this.id = id;
        this.name = name;
        this.calories = calories;
        this.carb = carb;
        this.protein = protein;
        this.fat = fat;
        this.recipe = recipe;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }

    public void setCarb(String carb) {
        this.carb = carb;
    }

    public void setProtein(String protein) {
        this.protein = protein;
    }

    public void setFat(String fat) {
        this.fat = fat;
    }

    public void setRecipe(String recipe) {
        this.recipe = recipe;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCalories() {
        return calories;
    }

    public String getCarb() {
        return carb;
    }

    public String getProtein() {
        return protein;
    }

    public String getFat() {
        return fat;
    }

    public String getRecipe() {
        return recipe;
    }
}
