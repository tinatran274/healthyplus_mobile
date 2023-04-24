package com.example.healthyplus.Model;

public class Product {
    private int id;
    private String name;
    private int cost;
    private String img;
    private String supplierName;
    private int type; // 1:Thuc pham 2:Do cong nghe

    public Product() {
        this.id = 0;
        this.name = "";
        this.cost = 0;
        this.img = "";
        this.supplierName = "";
        this.type = -1;
    }
    public Product(int id, String name, int cost, String img, String supllierName, int type) {
        this.id = id;
        this.name = name;
        this.cost = cost;
        this.img = img;
        this.supplierName = supllierName;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCost() {
        return cost;
    }

    public String getImg() {
        return img;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public int getType() {
        return type;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public void setSupllierName(String supllierName) {
        this.supplierName = supllierName;
    }

    public void setType(int type) {
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
