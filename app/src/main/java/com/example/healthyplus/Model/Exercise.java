package com.example.healthyplus.Model;

public class Exercise {
    private String name;
    private String id;
    private int caloriesPerHour;
    private String img;

    public Exercise() {
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getImg() {
        return img;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCaloriesPerHour(int caloriesPerHour) {
        this.caloriesPerHour = caloriesPerHour;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public int getCaloriesPerHour() {
        return caloriesPerHour;
    }
}
