package com.example.healthyplus.Model;

import java.io.Serializable;
import java.util.ArrayList;

public class Alarm implements Serializable {
    private String id;

    private String name;
    private String userId;
    private String hour;
    private String minute;
    private boolean status;
    private ArrayList<Boolean> day;

    public Alarm(String id, String name ,String userId, String hour, String minute, boolean status, ArrayList<Boolean> day) {
        this.id = id;
        this.name = name;
        this.userId = userId;
        this.hour = hour;
        this.minute = minute;
        this.status = status;
        this.day = day;
    }

    public Alarm() {
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
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getMinute() {
        return minute;
    }

    public void setMinute(String minute) {
        this.minute = minute;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public ArrayList<Boolean> getDay() {
        return day;
    }

    public void setDay(ArrayList<Boolean> day) {
        this.day = day;
    }

    @Override
    public String toString() {
        return "Alarm{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", userId='" + userId + '\'' +
                ", hour='" + hour + '\'' +
                ", minute='" + minute + '\'' +
                ", status=" + status +
                ", day=" + day +
                '}';
    }
}
