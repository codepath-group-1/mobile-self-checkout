package com.codepath.shopmyself.models;

/**
 * Created by supsingh on 11/29/2016.
 */

public class CreditCard {

    public static CreditCard card;
    public static CreditCard getInstance () {
        return card;
    }
    public static void setInstance (CreditCard saveCard) {
        card = saveCard;
    }


    private String cardNumber;
    private String cardExpiry;
    private String cardName;

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public void setCardExpiry(String cardExpiry) {
        this.cardExpiry = cardExpiry;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getCardExpiry() {
        return cardExpiry;
    }

    public String getCardName() {
        return cardName;
    }

    public CreditCard(String cardNumber, String cardExpiry, String cardName) {
        this.cardNumber = cardNumber;
        this.cardExpiry = cardExpiry;
        this.cardName = cardName;
    }
}
