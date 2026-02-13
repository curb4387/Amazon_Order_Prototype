package com.amazon.order;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.IOException;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.List;

public class ReceiptService {
    private NumberFormat moneyFormat = NumberFormat.getCurrencyInstance();
    private final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MMMM dd, yyyy");
    private final DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("h:mm a");
    private final DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("MMMM dd, yyyy 'at' h:mm a");
    // TODO: Define a DateTimeFormatter named 'timeFormat' (e.g., "h:mm a")

    public void sendOrderConfirmationEmail(Order order) {
        Customer c = order.getCustomer();
        System.out.println("\n-------------------------------------------------");
        System.out.println("EMAIL TO: " + c.getEmail());
        System.out.println("SUBJECT: Order Confirmation - #" + order.getOrderNumber());
        System.out.println("-------------------------------------------------\n");
        System.out.println("Hello " + c.getCustomerName() + ",\n");
        System.out.println("We've received your order!\nWe'll notify you when it ships.");
        System.out.println("Initial Est. Delivery: " + order.getEstimatedDeliveryDate().format(dateFormat));
        System.out.println("-------------------------------------------------\n");
    }

    // Method to create PDF invoice
    public void generatePdfInvoice(Order order) {
        String fileName = "Invoice_" + order.getOrderNumber() + ".pdf";

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            // --- TODO: PDF WRITING LOGIC ---
            // 1. Set Font (PDType1Font.HELVETICA_BOLD)
            // 2. Begin Text
            // 3. Set Leading and NewLines
            // 4. Draw Product Images (PDImageXObject)

            contentStream.beginText();
            contentStream.newLineAtOffset(50, 700);
            contentStream.showText("AMAZON OFFICIAL INVOICE");
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText("Customer: " + order.getCustomer().getCustomerName());
            contentStream.endText();

            contentStream.close();
            document.save(fileName);
            System.out.println("[LOG] PDF Invoice generated: " + fileName);

        } catch (IOException e) {
            System.err.println("Error generating PDF: " + e.getMessage());
        }
    }

    public void sendShipmentUpdate(Order order) {
        Shipment s = order.getShipment();

        if (s == null) {
            System.out.println("WARNING: Shipment information missing.");
            return;
        }

        System.out.println("\nSMS TO: " + order.getCustomer().getMobilePhone() + "\n--- [AMAZON NOTIFICATION] ---");
        switch (s.getStatus()) {
            case SHIPPED -> System.out.println("Your order #" + order.getOrderNumber() + " has shipped via " +
                s.getCarrier() + "\nTrack here: " + s.getTrackingNumber() + "\n");
            case IN_TRANSIT -> System.out.println("Your package is in transit.\nTracking: " +
                s.getTrackingNumber() + "\n");
            case DELAYED -> sendDelayNotification(order, s.getExpectedArrival());
            case DELIVERED -> System.out.println("Your package was " + order.getOrderStatus() + " to the front door on " +
                s.getExpectedArrival().minusHours(2).format(dateTimeFormat) + ".\n");
        }
    }

    public void sendInvoice(Order order, List<OrderItem> items) {
        Customer c = order.getCustomer();
        Payment p = order.getPayment();

        System.out.println("-------------------------------------------------");
        System.out.println("EMAIL TO: " + c.getEmail());
        System.out.println("SUBJECT: Amazon Invoice - #" + order.getOrderNumber());
        System.out.println("-------------------------------------------------");
        System.out.println("Order Number: " + order.getOrderNumber());
        System.out.println("Order Date: " + order.getEstimatedDeliveryDate().minusDays(2).format(dateFormat));
        System.out.println("Status: " + order.getOrderStatus());
        System.out.println("-------------------------------------------------");
        System.out.print("SOLD TO:\n" + c.getCustomerName() + "\n" + c.getEmail() + "\n" + c.getBillingAddress().getFormattedAddress());
        System.out.println("-------------------------------------------------\n");
//        System.out.println("ITEM                                   QTY   TOTAL");
        System.out.format("%-50.50s", "ITEM");
        System.out.format("%4s", "QTY");
        System.out.format("%10s", "TOTAL\n");

        // print out list of items and subtotal prices
        for (OrderItem i : items) {
            double itemTotal = i.getProduct().getPrice() * i.getQuantity();
            System.out.format("%-50.50s", i.getProduct().getDescription());
            System.out.format("%4d", i.getQuantity());
            System.out.format("%9.2f", itemTotal);
            System.out.print("\n");
        }

        // print out shipping and total
        System.out.println("-------------------------------------------------");
        System.out.print("Shipping & Handling:   $ ");
        System.out.format("%.2f", order.getShippingCost());
        System.out.print("\nGRAND TOTAL:           $ ");
        System.out.format("%.2f", order.getGrandTotal(items) + order.getShippingCost());
        System.out.println("\n-------------------------------------------------");
        System.out.println("PAID VIA: " + p.getIssuer() + " (" + p.getMaskedAccountNumber() + ")");
        System.out.println("-------------------------------------------------\n");
    }

    public void sendDelayNotification(Order order, LocalDateTime oldDate) {
        Shipment s = order.getShipment();
        oldDate = s.getExpectedArrival();
        s.updateDeliveryEstimate(2);
        int delayDays = s.getExpectedArrival().getDayOfMonth() - oldDate.getDayOfMonth();

        System.out.println("ALERT: Your package is delayed.\n" +
                "\nStatus: Transit delay due to carrier logistics." +
                "\nWAS: " + oldDate.format(dateFormat) +
                "\nNOW: " + s.getExpectedArrival().format(dateFormat) +
                ".\nCheck your email for more details.");
    }
}