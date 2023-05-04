package com.example.healthyplus.Model;

public class Product {
    private String id;
    private String name;
    private String cost;
    private String img;
    private String supplierName;
    private String type;

    public Product() {
        this.id = "";
        this.name = "";
        this.cost = "";
        this.img = "";
        this.supplierName = "";
        this.type = "";
    }
    public Product(String id, String name, String cost, String img, String supllierName, String type) {
        this.id = id;
        this.name = name;
        this.cost = cost;
        this.img = img;
        this.supplierName = supllierName;
        this.type = type;
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

    public String getType() {
        return type;
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

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", cost=" + cost +
                ", img='" + img + '\'' +
                ", supllierName='" + supplierName + '\'' +
                ", type=" + type +
                '}';
    }
}
