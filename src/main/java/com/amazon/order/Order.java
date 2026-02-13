package com.amazon.order;
import java.time.LocalDateTime;
import java.util.List;

public class Order {
    public enum OrderStatus { PENDING, PROCESSING, SHIPPED, DELIVERED }

    private String orderNumber;
    private Customer customer;
    private OrderStatus status;
    private LocalDateTime orderDate;
    private double shippingCost;
    private Payment payment;
    // for payment, it's possible to have more than 1 type of payment, ie. credit card and gift card
    // if we want to implement that, use an ArrayList of payments
    private Shipment shipment;

    public Order(String orderNumber, Customer customer, List<OrderItem> items, double shipping) {
        this.orderNumber = orderNumber;
        this.customer = customer;
        this.shippingCost = shipping; // $5.00 shipping default, $0.00 if Prime member
        this.orderDate = LocalDateTime.now();
        this.status = OrderStatus.PENDING;
    }

    public LocalDateTime getEstimatedDeliveryDate() {
        return orderDate.plusDays(2);
    }

    // Getters and Setters...
    public Customer getCustomer() { return customer; }
    public void setOrderStatus(OrderStatus status) { this.status = status; }
    public OrderStatus getOrderStatus() { return status; }
    public void setShipment(Shipment shipment) { this.shipment = shipment; }
    public Shipment getShipment() { return shipment;  }
    public String getOrderNumber() { return orderNumber; }
    public void setPayment(Payment payment) { this.payment = payment; }
    public Payment getPayment() { return payment; }
    public List<OrderItem> getItems(List<OrderItem> items) { return items; }

    public double getShippingCost() {
        if (customer.getMembership()) {
            shippingCost = 0.00;
        } else {
            shippingCost = 5.00;
        }
        return shippingCost;
    }

    public double getGrandTotal(List<OrderItem> items) {
        double tax = 1.06; // 6% tax rate
        double total = 0.00;
        for (OrderItem i : items) {
            total += i.getProduct().getPrice() * i.getQuantity();
        }
        return total * tax;
    }
}