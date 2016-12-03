package com.codepath.shopmyself.models;


public class User {

    private String email;
    private String password;
    private CreditCard card;

    public User() {
        //default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String email, String password, CreditCard card) {
        this.email = email;
        this.password = password;
        this.card = card;
    }
}
