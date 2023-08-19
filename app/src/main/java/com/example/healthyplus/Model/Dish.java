package com.example.healthyplus.Model;

import java.io.Serializable;
import java.util.ArrayList;

public class Dish implements Serializable {
    private String id;
    private String name;
    private double calo;
    private String img;

    private String creator;
    private double carb;
    private  double protein;
    private double fat;
    private ArrayList<String> recipe;
    private ArrayList<String> ingredients;

    public Dish() {
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getImg() {
        return img;
    }

    public void setFat(double fat) {
        this.fat = fat;
    }

    public double getFat() {
        return fat;
    }

    public Dish(String id, String name, String creator, String img, double calo, double carb, double fat, double protein, ArrayList<String> ingredients, ArrayList<String> recipe) {
        this.id = id;
        this.name = name;
        this.calo = calo;
        this.creator = creator;
        this.img = img;
        this.carb = carb;
        this.fat = fat;
        this.protein = protein;
        this.recipe = recipe;
        this.ingredients = ingredients;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCalo(double calo) {
        this.calo = calo;
    }

    public void setCarb(double carb) {
        this.carb = carb;
    }

    public void setProtein(double protein) {
        this.protein = protein;
    }

    public void setRecipe(ArrayList<String> recipe) {
        this.recipe = recipe;
    }

    public void setIngredients(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getCalo() {
        return calo;
    }

    public double getCarb() {
        return carb;
    }

    public double getProtein() {
        return protein;
    }

    public ArrayList<String> getRecipe() {
        return recipe;
    }

    public ArrayList<String> getIngredients() {
        return ingredients;
    }
}
