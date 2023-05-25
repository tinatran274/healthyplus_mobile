package com.example.healthyplus.Model;

import java.io.Serializable;

public class Ingredient implements Serializable {
    private String name;
    private String id;
    private double calo;
    private String img;
    private double carb;
    private double weight;
    private int protein;
    private int fat;

    public Ingredient() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCalo(double calo) {
        this.calo = calo;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public void setCarb(double carb) {
        this.carb = carb;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setProtein(int protein) {
        this.protein = protein;
    }

    public void setFat(int fat) {
        this.fat = fat;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public double getCalo() {
        return calo;
    }

    public String getImg() {
        return img;
    }

    public double getCarb() {
        return carb;
    }

    public double getWeight() {
        return weight;
    }

    public int getProtein() {
        return protein;
    }

    public int getFat() {
        return fat;
    }
}