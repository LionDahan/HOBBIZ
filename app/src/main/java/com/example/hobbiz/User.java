package com.example.hobbiz;

public class User {
    String email,fullName;

    public User(String userEmail, String userName){}

    public User(String address, String city, String email, String fullName, String phone ){
        this.email = email;
        this.fullName = fullName;

    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }


}
