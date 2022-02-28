package com.example.hobbiz.Model;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.hobbiz.MyApplication;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;

import java.util.HashMap;
import java.util.Map;
@Entity
public class Hobbiz implements Parcelable {
    @PrimaryKey
    @NonNull
    private String id ="";
    private String hobby_Name,age, city, contact, description, image, userId;
    private boolean isDeleted;

    Long lastUpdated = new Long(0);

    public Hobbiz(){}

    public Hobbiz(String hobby_Name, String age, String city, String contact, String description){
        this.hobby_Name= hobby_Name;
        this.age=age;
        this.city = city;
        this.contact=contact;
        this.description= description;
    }

    final static String TIME = "timestamp";


    public String getId(){return id;}

    public void setId(String id){this.id= id;}

    public String getHobby_Name() {
        return hobby_Name;
    }

    public String getCity() {
        return city;
    }

    public String getAge() {
        return age;
    }
    public String getUserId(){return userId;}

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContact() {
        return contact;
    }

    public String getDescription(){
        return description;
    }

    public String getImage(){return image;}

    public void setImage(String image) {
        this.image = image;
    }

    public void setHobby_Name(String hobby_Name) {
        this.hobby_Name = hobby_Name;
    }

    public void setAge(String age) { this.age = age; }

    public void setCity(String city) { this.city = city; }

    public void setContact(String contact) { this.contact = contact; }

    public void setDescription(String description){this.description= description;}

    public void setLastUpdated(Long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
    public boolean getIsDeleted(){
        return isDeleted;
    }
    public void setIsDeleted(boolean delete){
        isDeleted = delete;
    }

    static Hobbiz HobbizFromJson(Map<String,Object> json){

        String name = (String) json.get("hobby_name");
        String city = (String) json.get("city");
        String age = (String) json.get("age");
        String contact = (String) json.get("contact");
        String description = (String) json.get("description");
        String image= (String) json.get("image");
        String userId= (String)json.get("userId");
        Boolean isDeleted = (Boolean) json.get("isDeleted");

        Hobbiz hobby = new Hobbiz(name,city,age,contact,description);
        hobby.setImage(image);
        hobby.setUserId(userId);
        hobby.setIsDeleted(isDeleted);
        Timestamp ts = (Timestamp)json.get(Constants.LAST_UPDATED);
        hobby.setLastUpdated(new Long(ts.getSeconds()));

        return hobby;
    }
    static Long getLocalLastUpdated(){
        Long localLastUpdate = MyApplication.getContext()
                .getSharedPreferences("TAG", Context.MODE_PRIVATE)
                .getLong("HOBBIZ_LAST_UPDATE",0);
        return localLastUpdate;
    }

    static void setLocalLastUpdated(Long date){
        SharedPreferences.Editor editor = MyApplication.getContext()
                .getSharedPreferences("TAG", Context.MODE_PRIVATE).edit();
        editor.putLong("HOBBIZ_LAST_UPDATE",date);
        editor.commit();
    }

    public Long getLastUpdated(){return lastUpdated;}

    public static final Creator<Hobbiz> CREATOR = new Creator<Hobbiz>() {
        @Override
        public Hobbiz createFromParcel(Parcel in) {
            return new Hobbiz(in);
        }

        @Override
        public Hobbiz[] newArray(int size) {
            return new Hobbiz[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(hobby_Name);
        parcel.writeString(city);
        parcel.writeString(age);
        parcel.writeString(contact);
        parcel.writeString(description);
        parcel.writeString(image);
        parcel.writeString(userId);

        parcel.writeByte((byte) (isDeleted ? 1 : 0));
        if (lastUpdated == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(lastUpdated);
        }
    }

    public Hobbiz(Parcel in) {
        id = in.readString();
        hobby_Name = in.readString();
        city = in.readString();
        age = in.readString();
        contact = in.readString();
        description = in.readString();
        image = in.readString();
        userId= in.readString();

        isDeleted = in.readByte() != 0;
        if (in.readByte() == 0) {
            lastUpdated = null;
        } else {
            lastUpdated = in.readLong();
        }

    }
    public Map<String, Object> toJson() {
        Map<String, Object> dbHobby = new HashMap<>();

        dbHobby.put("hobby_name", this.getHobby_Name());
        dbHobby.put("city", this.getCity());
        dbHobby.put("age", this.getAge());
        dbHobby.put("contact", this.getContact());
        dbHobby.put("description", this.getDescription());
        dbHobby.put("timestamp", FieldValue.serverTimestamp());
        dbHobby.put("image", this.getImage());
        dbHobby.put("userId", userId);
        dbHobby.put("isDeleted", getIsDeleted());

        return dbHobby;
    }



}
