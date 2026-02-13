package com.amazon.order;

public class Address {
    private String street;
    private String city;
    private String state;
    private String zip;
    private String country;

    public Address(String street, String city, String state, String zip, String country) {
        this.street = street;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.country = country;
    }

    // toString will generate the address
    @Override
    public String toString() {
        return street + "\n" + city + ", " + state + " " + zip + "\n" + country + "\n";
    }

    public String getFormattedAddress() {
        return toString();
    }

    // Standard Getters
    public String getStreet() { return street; }
    public String getCity() { return city; }
    public String getState() { return state; }
    public String getZip() { return zip; }
    public String getCountry() { return country; }
}