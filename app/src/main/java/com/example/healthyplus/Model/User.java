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
    private boolean isPremium;

    public User() {
        this.id = "";
        this.name = "";
        this.gender = 0;
        this.age = 0;
        this.height = 0;
        this.weight = 0;
        this.aim = 0;
        this.exerciseFrequency = 0;
        this.isPremium = false;
    }
    public User(String id, String name, int gender, int age, int height, int weight, int aim, int exerciseFrequency, boolean isPremium) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.aim = aim;
        this.exerciseFrequency = exerciseFrequency;
        this.isPremium = isPremium;
    }

    public boolean isPremium() {
        return isPremium;
    }

    public void setPremium(boolean premium) {
        isPremium = premium;
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
        return (float) Math.floor(this.weight/(this.height*0.01*this.height*0.01)* 100)/100;
    }
    public int TTDECal() {
        float bmr;
        if(this.gender==0) {
            bmr = (float) (655+(9.6*this.weight)+(1.8*this.height)-(4.7*this.age));
        } else {
            bmr = (float) (66 + (13.7*this.weight)+(5 *this.height)-(6.8*this.age));
        }
        if(exerciseFrequency == 0)
            return (int) Math.floor(bmr*1.2);
        else if(exerciseFrequency == 1)
            return (int) Math.floor(bmr*1.375);
        else if(exerciseFrequency == 2)
            return (int) Math.floor(bmr*1.725);
        else if(exerciseFrequency == 3)
            return (int) Math.floor(bmr*1.9);
        return 0;
    }
    public int WaterCal(){
        int water = 0;
        if(this.gender == 1){
            if(exerciseFrequency == 0)
                water += 35*this.weight + (exerciseFrequency + 1.2)*500;
            else if(exerciseFrequency == 1)
                water += 35*this.weight + (exerciseFrequency + 0.35)*500;
            else if(exerciseFrequency == 2)
                water += 35*this.weight + (exerciseFrequency - 0.5)*500;
            else if(exerciseFrequency == 3)
                water += 35*this.weight + (exerciseFrequency - 1)*500;

        }
        else {
            if(exerciseFrequency ==0)
             water += 31*this.weight + (exerciseFrequency + 1.2)*500;
            else if(exerciseFrequency == 1)
                water += 31*this.weight + (exerciseFrequency + 0.35)*500;
            else if(exerciseFrequency == 2)
                water += 31*this.weight + (exerciseFrequency - 0.5)*500;
            else if(exerciseFrequency == 3)
                water += 31*this.weight + (exerciseFrequency - 1)*500;
        }
        return water;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", gender=" + gender +
                ", age=" + age +
                ", height=" + height +
                ", weight=" + weight +
                ", aim=" + aim +
                ", exerciseFrequency=" + exerciseFrequency +
                ", isPremium=" + isPremium +
                '}';
    }
}
