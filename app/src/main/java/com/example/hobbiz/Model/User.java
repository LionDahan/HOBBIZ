package com.example.hobbiz.Model;

import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Map;

@Entity
public class User implements Parcelable{
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
    protected User(Parcel in) {
        email = in.readString();
        userName = in.readString();
        id = in.readString();
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


    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    static User fromJson(Map<String,Object> json) {
        String name = (String)json.get("full_name");
        String email = (String)json.get("e_mail");
        User u = new User(name, email);
        return u;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(email);
        parcel.writeString(userName);
        parcel.writeString(id);
    }


}
