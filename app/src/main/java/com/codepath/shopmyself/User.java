package com.codepath.shopmyself;


public class User {
    private String email;
    private String password;

    public User() {
        //default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
