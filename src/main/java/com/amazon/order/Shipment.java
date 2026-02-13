package com.amazon.order;
import java.time.LocalDateTime;

public class Shipment {
    // Nested Enum for tracking the physical location of the box
    public enum Status { SHIPPED, IN_TRANSIT, DELAYED, DELIVERED }
    public enum ShipmentSpeed { STANDARD, TWO_DAY, NEXT_DAY }

    private String trackingNumber;
    private String carrier;
    private Status status;
    private LocalDateTime expectedArrival;

    public Shipment(String trackingNumber, String carrier, ShipmentSpeed speed, LocalDateTime initialEstimate) {
        this.trackingNumber = trackingNumber;
        this.carrier = carrier;
        this.expectedArrival = initialEstimate;
        this.status = Status.SHIPPED;
    }

    public void updateDeliveryEstimate(int daysToAdd) {
        if (expectedArrival == null) {
            return;
        }
        expectedArrival = expectedArrival.plusDays(daysToAdd);
    }

    public String getTrackingNumber() { return trackingNumber; }
    public String getCarrier() { return carrier; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
    public LocalDateTime getExpectedArrival() { return expectedArrival; }
    public void setExpectedArrival(LocalDateTime expectedArrival) { this.expectedArrival = expectedArrival; }
}