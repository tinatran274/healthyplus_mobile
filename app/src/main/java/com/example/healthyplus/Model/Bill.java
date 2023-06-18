package com.example.healthyplus.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

public class Bill implements Serializable {

    private String id;
    private String userID;
    private String address;
    private Map<String, Object> products;
    private String delivery;
    private String time;
    private String pay;
    private boolean status;

    private boolean returnPay;
    private String total;

    private int date;

    public Bill() {
    }

    public Bill(String id, String userID, String address, Map<String, Object> products, String delivery, String time, String pay, boolean status,boolean returnPay, String total, int date) {
        this.id = id;
        this.userID = userID;
        this.address = address;
        this.products = products;
        this.delivery = delivery;
        this.time = time;
        this.pay = pay;
        this.status = status;
        this.returnPay = returnPay;
        this.total = total;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Map<String, Object> getProducts() {
        return products;
    }

    public void setProducts(Map<String, Object> products) {
        this.products = products;
    }

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPay() {
        return pay;
    }

    public void setPay(String pay) {
        this.pay = pay;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
    public int getDate() {
        return date;
    }

    public boolean isReturnPay() {
        return returnPay;
    }

    public void setReturnPay(boolean returnPay) {
        this.returnPay = returnPay;
    }

    public void setDate(int date) {
        this.date = date;
    }
}
