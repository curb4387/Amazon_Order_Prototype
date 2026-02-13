//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.amazon.order;

import com.amazon.order.Payment.PaymentType;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

public class PdfInvoiceGenerator {
    private void drawRightAlignedString(PDPageContentStream contentStream, PDFont font, int fontSize, String text, float xRight, float y) throws IOException {
        float stringWidth = font.getStringWidth(text) * (float)fontSize / 1000.0F;
        contentStream.beginText();
        contentStream.setFont(font, (float)fontSize);
        contentStream.newLineAtOffset(xRight - stringWidth, y);
        contentStream.showText(text);
        contentStream.endText();
    }

    public void generatePdfInvoice(Order order) {
        String fileName = "Invoice_" + order.getOrderNumber() + ".pdf";
        Customer c = order.getCustomer();
        ArrayList<Payment> payments = order.getPayments();
        Shipment s = order.getShipment();

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.addRect(50.0F, 730.0F, 500.0F, 50.0F);
            contentStream.stroke();
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12.0F);
            contentStream.newLineAtOffset(60.0F, 760.0F);
            contentStream.showText("Order Summary");
            contentStream.setFont(PDType1Font.HELVETICA, 9.0F);
            contentStream.newLineAtOffset(0.0F, -15.0F);
            LocalDateTime var10001 = order.getOrderDate();
            contentStream.showText("Order placed: " + var10001.format(DateTimeFormatter.ofPattern("MMMM dd, yyyy")));
            contentStream.newLineAtOffset(200.0F, 0.0F);
            contentStream.endText();
            int box2Y = 600;
            int box2Height = 120;
            contentStream.addRect(50.0F, (float)box2Y, 500.0F, (float)box2Height);
            contentStream.stroke();
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 9.0F);
            contentStream.newLineAtOffset(60.0F, (float)(box2Y + 105));
            contentStream.showText("Ship to");
            contentStream.newLineAtOffset(160.0F, 0.0F);
            contentStream.showText("Payment method");
            contentStream.newLineAtOffset(160.0F, 0.0F);
            contentStream.showText("Order Summary");
            contentStream.endText();
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA, 8.0F);
            contentStream.setLeading(10.0F);
            contentStream.newLineAtOffset(60.0F, (float)(box2Y + 90));
            contentStream.showText(c.getCustomerName());

            for(String line : c.getShippingAddress().getFormattedAddress().split("\n")) {
                contentStream.newLine();
                contentStream.showText(line);
            }

            contentStream.endText();
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA, 8.0F);
            contentStream.setLeading(11.0F);
            contentStream.newLineAtOffset(220.0F, (float)(box2Y + 90));

            for(Payment p : payments) {
                Payment.PaymentType type = p.getType();
                if (type != PaymentType.GIFT_CARD && type != PaymentType.REWARD_POINTS) {
                    String issuer = p.getIssuer();
                    String accountNum = p.getMaskedAccountNumber();
                    String lastFour = "****";
                    if (accountNum != null && accountNum.length() >= 4) {
                        lastFour = accountNum.substring(accountNum.length() - 4);
                    }

                    contentStream.showText(issuer + " ending in " + lastFour);
                    contentStream.newLine();
                }
            }

            contentStream.endText();
            float labelX = 380.0F;
            float valueRightX = 540.0F;
            float currentLineY = (float)(box2Y + 90);
            float leading = 11.0F;
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA, 8.0F);
            contentStream.setLeading(leading);
            contentStream.newLineAtOffset(labelX, currentLineY);
            contentStream.showText("Item(s) Subtotal:");
            contentStream.newLine();
            contentStream.showText("Shipping:");
            contentStream.newLine();
            contentStream.showText("Total before tax:");
            contentStream.newLine();
            contentStream.showText("Estimated tax:");
            contentStream.newLine();

            for(Payment p : payments) {
                if (p.getType() == PaymentType.REWARD_POINTS) {
                    contentStream.showText("Reward Points:");
                    contentStream.newLine();
                } else if (p.getType() == PaymentType.GIFT_CARD) {
                    contentStream.showText("Gift Card:");
                    contentStream.newLine();
                }
            }

            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 9.0F);
            contentStream.showText("Grand Total:");
            contentStream.endText();
            PDType1Font var10002 = PDType1Font.HELVETICA;
            Object[] var10005 = new Object[]{order.calculateItemsSubtotal()};
            this.drawRightAlignedString(contentStream, var10002, 8, "$" + String.format("%.2f", var10005), valueRightX, currentLineY);
            float var41 = currentLineY - leading;
            double shippingCost = order.getShipment() != null ? order.getShippingCost() : (double)0.0F;
            this.drawRightAlignedString(contentStream, PDType1Font.HELVETICA, 8, "$" + String.format("%.2f", shippingCost), valueRightX, var41);
            var41 -= leading;
            this.drawRightAlignedString(contentStream, PDType1Font.HELVETICA, 8, "$" + String.format("%.2f", order.calculateItemsSubtotal()), valueRightX, var41);
            var41 -= leading;
            this.drawRightAlignedString(contentStream, PDType1Font.HELVETICA, 8, "$" + String.format("%.2f", order.calculateTax()), valueRightX, var41);
            var41 -= leading;

            for(Payment p : payments) {
                if (p.getType() == PaymentType.GIFT_CARD || p.getType() == PaymentType.REWARD_POINTS) {
                    Object[] var57 = new Object[]{p.getPaymentAmount()};
                    String amountText = "-$" + String.format("%.2f", var57);
                    this.drawRightAlignedString(contentStream, PDType1Font.HELVETICA, 8, amountText, valueRightX, var41);
                    var41 -= leading;
                }
            }

            this.drawRightAlignedString(contentStream, PDType1Font.HELVETICA_BOLD, 9, "$" + String.format("%.2f", order.calculateGrandTotal()), valueRightX, var41);
            int box3Y = 320;
            int box3Height = 260;
            int itemY = box3Y + box3Height - 50;

            for(OrderItem item : order.getOrderItems()) {
                int qty = item.getQuantity();

                try {
                    String imgPath = "src/main/resources/images/" + item.getProduct().getProductId() + ".jpg";
                    PDImageXObject pdImage = PDImageXObject.createFromFile(imgPath, document);
                    contentStream.drawImage(pdImage, 60.0F, (float)(itemY - 40), 50.0F, 50.0F);
                } catch (Exception var29) {
                    contentStream.addRect(60.0F, (float)(itemY - 40), 50.0F, 50.0F);
                    contentStream.stroke();
                }

                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 8.0F);
                contentStream.newLineAtOffset(120.0F, (float)itemY);
                String desc = item.getProduct().getDescription();
                if (desc.length() > 60) {
                    desc = desc.substring(0, 57) + "...";
                }

                String displayDesc = qty > 1 ? desc + " (Qty: " + qty + ")" : desc;
                contentStream.showText(displayDesc);
                contentStream.endText();
                double lineTotal = item.calculateItemTotal();
                this.drawRightAlignedString(contentStream, PDType1Font.HELVETICA_BOLD, 8, "$" + String.format("%.2f", lineTotal), valueRightX, (float)itemY);
                itemY -= 70;
            }

            float footerY = 80.0F;
            contentStream.setStrokingColor(200, 200, 200);
            contentStream.moveTo(50.0F, footerY + 40.0F);
            contentStream.lineTo(550.0F, footerY + 40.0F);
            contentStream.stroke();
            contentStream.setStrokingColor(0, 0, 0);
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA, 7.0F);
            contentStream.setLeading(9.0F);
            contentStream.newLineAtOffset(60.0F, footerY + 25.0F);
            contentStream.showText("For more information on your orders, please visit your Account page.");
            contentStream.newLine();
            contentStream.showText("Items may be returned within 30 days of delivery. See our Return Policy for details.");
            contentStream.endText();
            float barcodeX = 430.0F;
            float barcodeY = footerY - 10.0F;

            for(int i = 0; i < 40; ++i) {
                float width = i % 3 == 0 ? 2.0F : 0.5F;
                contentStream.addRect(barcodeX + (float)(i * 2), barcodeY, width, 25.0F);
                contentStream.fill();
            }

            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA, 7.0F);
            contentStream.newLineAtOffset(barcodeX + 5.0F, barcodeY - 10.0F);
            contentStream.showText("*" + order.getOrderNumber() + "*");
            contentStream.endText();
            contentStream.close();
            document.save(fileName);
            System.out.println("[SUCCESS] Invoice Generated: " + fileName);
        } catch (IOException e) {
            System.err.println("PDF Generation Failed: " + e.getMessage());
        }

    }
}