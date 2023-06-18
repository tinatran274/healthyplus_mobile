package com.example.healthyplus.Model;

public class BuyProduct {
    private String id;
    private String name;
    private String cost;
    private String img;
    private String supplierName;
    private String num;

    public BuyProduct(String id, String name, String cost, String img, String supplierName, String num) {
        this.id = id;
        this.name = name;
        this.cost = cost;
        this.img = img;
        this.supplierName = supplierName;
        this.num = num;
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

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }
}
