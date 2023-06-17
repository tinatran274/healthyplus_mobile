package com.example.healthyplus.Model;

import java.io.Serializable;

public class User implements Serializable {
    private String id;
    private String name;
    private int gender; // 0:Nu 1:Nam
    private int age;
    private int height;
    private int weight;
    private int aim; // 2:Tang 1:Giu 0:Giam
    private int exerciseFrequency; // 0:Khong 1:Nhe 2:Vua 3:Nang

    public User() {
        this.id = "";
        this.name = "";
        this.gender = 0;
        this.age = 0;
        this.height = 0;
        this.weight = 0;
        this.aim = 0;
        this.exerciseFrequency = 0;
    }
    public User(String id, String name, int gender, int age, int height, int weight, int aim, int exerciseFrequency) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.aim = aim;
        this.exerciseFrequency = exerciseFrequency;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getAim() {
        return aim;
    }

    public void setAim(int aim) {
        this.aim = aim;
    }

    public int getExerciseFrequency() {
        return exerciseFrequency;
    }

    public void setExerciseFrequency(int exerciseFrequency) {
        this.exerciseFrequency = exerciseFrequency;
    }
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
    public float BMICal() {
        return (float) Math.floor(this.weight/(this.height*0.02)* 100)/100;
    }
    public int TTDECal() {
        float bmr;
        if(this.gender==0) {
            bmr = (float) (88.362+(13.397*this.weight)+(4.799*this.height)-(5.677*this.age));
        } else {
            bmr = (float) (447.593+(9.247*this.weight)+(3.098 *this.height)-(4.33*this.age));
        }
        return (int) Math.floor(bmr*this.exerciseFrequency* 100)/100;
    }
}
