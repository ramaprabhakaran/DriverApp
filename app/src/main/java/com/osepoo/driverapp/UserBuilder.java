package com.osepoo.driverapp;

public class UserBuilder {
    private String email;
    private String password;

    public UserBuilder setEmail(String email) {
        this.email = email;
        return this;
    }

    public UserBuilder setPassword(String password) {
        this.password = password;
        return this;
    }

    public User createUser() {
        return new User(email, password);
    }
}