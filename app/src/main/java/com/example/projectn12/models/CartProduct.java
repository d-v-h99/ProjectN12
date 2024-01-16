package com.example.projectn12.models;

import java.util.List;

public class CartProduct {
    String currentTime;
    String currentDate;
    String productName;
    Integer  productPrice;
    Integer totalQuantity;
    String images;

    public CartProduct() {
    }

    public CartProduct(String currentTime, String currentDate, String productName, Integer productPrice, Integer totalQuantity, String images) {
        this.currentTime = currentTime;
        this.currentDate = currentDate;
        this.productName = productName;
        this.productPrice = productPrice;
        this.totalQuantity = totalQuantity;
        this.images = images;
    }

    public CartProduct(String productName, Integer productPrice, Integer totalQuantity, String images) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.totalQuantity = totalQuantity;
        this.images = images;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(Integer productPrice) {
        this.productPrice = productPrice;
    }

    public Integer getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(Integer totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }
}
