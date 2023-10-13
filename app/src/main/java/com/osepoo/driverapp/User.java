package com.osepoo.driverapp;

import javax.security.auth.callback.PasswordCallback;

public class User {
    private String email;
    private String password; // In a real application, consider using secure password hashing

    // Default constructor (required for Firebase)
    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public UserBuilder setEmail(String userId) {

        return null;
    }
}
