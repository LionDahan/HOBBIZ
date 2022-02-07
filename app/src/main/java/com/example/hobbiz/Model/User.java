package com.example.hobbiz.Model;

public class User {
    String email, userName;

    public User(){}
    public User(String email, String userName ){
        this.email = email;
        this.userName = userName;

    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return userName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFullName(String userName) {
        this.userName = userName;
    }


}
