package com.example.healthyplus.Model;

import java.io.Serializable;

public class Account implements Serializable {
    private String id;
    private String userID;
    private String userName;
    private String email;
    private String password;

    public Account() {
    }

    public Account(String id, String userID, String userName, String email, String password) {
        this.id = id;
        this.userID = userID;
        this.userName = userName;
        this.email = email;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public String getId() {
        return id;
    }

    public String getUserID() {
        return userID;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
