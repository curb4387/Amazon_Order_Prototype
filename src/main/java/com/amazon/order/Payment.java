package com.amazon.order;

public class Payment {
    enum PaymentType { CREDIT_CARD, BANK_TRANSFER, GIFT_CARD }
    private String issuer;
    private String accountNumber; // Example: "1234567890123456"

    public Payment(PaymentType type, String accountNumber, String issuer) {
        this.accountNumber = accountNumber;
        this.issuer = issuer;
    }

    public String getMaskedAccountNumber() {
        String hiddenNum = "**** **** **** ";
        String lastFour = accountNumber.substring(accountNumber.length() - 4);
        return hiddenNum + lastFour;
    }

    public String getIssuer() { return issuer; }
}