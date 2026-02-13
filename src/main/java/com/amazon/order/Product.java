package com.amazon.order;

public class Product {
    // Shared Enumeration for Product State
    public enum Condition { New, Used, Refurbished }

    private String productId;
    private String description;
    private double price;
    private Condition condition;
    private String imagePath;
//    private String soldBy; ie. Amazon, another company
//    private String suppliedBy; ie. Amazon, another supplier

    public Product(String productId, String description, double price, Condition condition, String imagePath) {
        this.productId = productId;
        this.description = description;
        this.price = price;
        this.condition = condition;
        this.imagePath = imagePath;
//         this.soldBy = soldBy;
//         this.suppliedBy = suppliedBy;
    }

    public String getProductId() { return productId; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }
    public Condition getCondition() { return condition; }
    public String getImagePath() { return imagePath; }

    @Override
    public String toString() {
        return description + " (" + condition + ") - " + price;
    }
}