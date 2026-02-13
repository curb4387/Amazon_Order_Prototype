package com.amazon.order;

public class Customer {
    private String customerId;
    private String customerName;
    private String email;
    private String mobilePhone;
    private boolean primeMember;

    // TODO: Create two Address fields: one for billing and one for shipping
    // This demonstrates Composition (A Customer HAS-AN Address)
    private Address billingAddress;
    private Address shippingAddress;

    public Customer(String customerId, String customerName, String email, String mobilePhone, Address billing, Address shipping, Boolean membership) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.email = email;
        this.mobilePhone = mobilePhone;
        this.billingAddress = billing;
        this.shippingAddress = shipping;
        this.primeMember = membership;
    }

    public boolean isGiftOrder() {
        // Hint: Compare the street property of both address objects
        if (billingAddress == shippingAddress) {
            return true;
        } else {
            return false;
        }
    }

    // TODO: Generate Getters for customerName and shippingAddress
    public String getCustomerName() {
        return customerName;
    }
    public String getEmail() { return email; }
    public String getMobilePhone() { return mobilePhone; }
    public Address getShippingAddress() { return shippingAddress; }
    public Address getBillingAddress() {
        return billingAddress;
    }
    public boolean getMembership() { return primeMember; }
}