package com.qnally.shappapp.Model;

import java.io.Serializable;

public class Order implements Serializable {

    private String ProductName, Quantity, Price, Image;

    public Order() {
    }

    public Order(String productName, String quantity, String price, String image) {
        ProductName = productName;
        Quantity = quantity;
        Price = price;
        Image = image;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
