package com.example.healthyplus.Model;

import java.io.Serializable;

public class Product implements Serializable {
    private String id;
    private String name;
    private String cost;
    private String img;
    private String supplierName;

    public Product() {
    }
    public Product(String id, String name, String cost, String img, String supllierName) {
        this.id = id;
        this.name = name;
        this.cost = cost;
        this.img = img;
        this.supplierName = supllierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCost() {
        return cost;
    }

    public String getImg() {
        return img;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public void setSupllierName(String supllierName) {
        this.supplierName = supllierName;
    }


    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", cost=" + cost +
                ", img='" + img + '\'' +
                ", supllierName='" + supplierName + '\'' +
                '}';
    }
}
