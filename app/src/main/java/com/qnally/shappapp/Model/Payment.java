package com.qnally.shappapp.Model;

public class Payment {

    private String creditNumber;
    private String creditExp;
    private String creditCvv;
    private String cardType;

    public Payment() {
    }

    public Payment(String creditNumber, String creditExp, String creditCvv, String type) {
        this.creditNumber = creditNumber;
        this.creditExp = creditExp;
        this.creditCvv = creditCvv;
        cardType = type;
    }

    public String getCreditNumber() {
        return creditNumber;
    }

    public void setCreditNumber(String creditNumber) {
        this.creditNumber = creditNumber;
    }

    public String getCreditExp() {
        return creditExp;
    }

    public void setCreditExp(String creditExp) {
        this.creditExp = creditExp;
    }

    public String getCreditCvv() {
        return creditCvv;
    }

    public void setCreditCvv(String creditCvv) {
        this.creditCvv = creditCvv;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }
}
