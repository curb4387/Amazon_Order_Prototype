package com.amazon.order;

public class OrderItem {
    private Product product;
    private int quantity;

    public OrderItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    /**
     * TODO: IMPLEMENT THIS METHOD
     * Calculate the total for this line item (price * quantity).
     * Hint: You need to call a method on the 'product' object to get the price.
     */
    public double calculateItemTotal() {
        double itemPrice = product.getPrice();
        return itemPrice * quantity;
    }

    public Product getProduct() { return product; }
    public int getQuantity() { return quantity; }
}