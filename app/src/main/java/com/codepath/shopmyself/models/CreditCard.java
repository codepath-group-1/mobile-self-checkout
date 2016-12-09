package com.codepath.shopmyself.models;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;

public class CreditCard {

    public static String key = "creditCard";
    public static String cardNumberKey = "cardNumber";
    public static String cardExpiryKey = "cardExpiry";
    public static String cardNameKey = "cardName";

    private String cardNumber;
    private String cardExpiry;
    private String cardName;

    public CreditCard(String cardNumber, String cardExpiry, String cardName) {
        this.cardNumber = cardNumber;
        this.cardExpiry = cardExpiry;
        this.cardName = cardName;
    }

    public CreditCard(DataSnapshot dataSnapshot) {
        Map<String, Object> map = (Map<String, Object>)dataSnapshot.getValue();

        this.cardNumber = (String)map.get(cardNumberKey);
        this.cardExpiry = (String)map.get(cardExpiryKey);
        this.cardName = (String)map.get(cardNameKey);
    }

    public void save() {
        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
        DatabaseReference mDatabase
            = FirebaseDatabase.getInstance().getReference();
        String mUserId = mFirebaseUser.getUid();
        mDatabase.child("users").child(mUserId).child(key).setValue(this);
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

}
