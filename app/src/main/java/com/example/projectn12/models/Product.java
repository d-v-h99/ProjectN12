package com.example.projectn12.models;

import android.net.Uri;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Product implements Serializable {
    private String name;
    private String category;
    private float price;
    private Integer quantity;
    private String description;
    private List<String> images;
    private List<Uri> uriImg;

    public Product() {
        name="";
        category="";
        price=0;
        quantity=1;
        description="";
        images=new ArrayList<>();
        uriImg=new ArrayList<>();
    }

    public Product(String name, String category, float price, Integer quantity, String description, List<String> images) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
        this.description = description;
        this.images = images;
    }
    public void setDataProduct(String name, String description, float price , List<Uri> uriImg){
        this.name = name;
        this.description = description;
        this.price = price;
        this.uriImg = uriImg;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public List<Uri> getUriImg() {
        return uriImg;
    }

    public void setUriImg(List<Uri> uriImg) {
        this.uriImg = uriImg;
    }
}
