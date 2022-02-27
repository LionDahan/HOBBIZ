package com.example.hobbiz.Model;

import android.content.SharedPreferences;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Map;

@Entity
public class User {
    String userName;
    @PrimaryKey
    @NonNull
    String email;
    String id;

    public User(){}

    public User(String email, String userName ){
        this.email = email;
        this.userName = userName;

    }
    public User(String email, String userName, String id ){
        this.email = email;
        this.userName = userName;
        this.id= id;
    }

    public String getId() {
        return id;
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


    static User fromJson(Map<String,Object> json) {
        String name = (String)json.get("full_name");
        String email = (String)json.get("e_mail");
        User u = new User(name, email);
        return u;
    }

    public static void userToSharedPreference(User user, SharedPreferences.Editor editor) {
        editor.putString(Constants.USER + "full_name", user.getFullName());
        editor.putString(Constants.USER + "e_mail", user.getEmail());

    }

    public static User userFromSharedPreference(SharedPreferences sp) {
        String fname = sp.getString(Constants.USER + "full_name", null);
        String email = sp.getString(Constants.USER + "e_mail", null);
        String id = sp.getString(Constants.USER + "id", null);

        return new User( email, fname, id);
    }

    public static void logoutUserFromSP(SharedPreferences.Editor editor) {
        editor.remove(Constants.USER + "full_name");
        editor.remove(Constants.USER + "e_mail");
        editor.remove(Constants.USER + "id");
    }
}
