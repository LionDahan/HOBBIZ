package com.example.hobbiz.Model;
import com.example.hobbiz.MyApplication;
import com.google.firebase.Timestamp;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;

import java.util.Map;

public class Hobbiz {
    String hobby_Name,age, city, contact, description;
    Uri image;
    public final static String LAST_UPDATED = "LAST_UPDATED";

    Long lastUpdated = new Long(0);

    final static String ID = "id";

    final static String CITY = "city";
    final static String NAME_HOBBY = "hobby_Name";
    final static String CONTACT = "contact";
    final static String DESCRIPTION = "description";
    final static String AGE = "age";


    final static String TIME = "timestamp";


    public Hobbiz(){}

    public Hobbiz(String hobby_Name,String city,String age,String contact,String description){
        this.hobby_Name = hobby_Name;
        this.city = city;
        this.age = age;
        this.contact = contact;
        this.description= description;

    }


    public String getHobby_Name() {
        return hobby_Name;
    }

    public String getCity() {
        return city;
    }

    public String getAge() {
        return age;
    }


    public String getContact() {
        return contact;
    }

    public String getDescription(){
        return description;
    }

    public Uri getImage(){return image;}

    public void setImage(Uri image) {
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

    static Hobbiz HobbizFromJson(Map<String,Object> json){

        String name = (String) json.get("name");
        String city = (String) json.get("city");
        String age = (String) json.get("age");
        String contact = (String) json.get("contact");
        String description = (String) json.get("description");


        Hobbiz hobby = new Hobbiz(name,city,age,contact,description);
        Timestamp ts = (Timestamp)json.get(LAST_UPDATED);
        hobby.setLastUpdated(new Long(ts.getSeconds()));

        return hobby;
    }
    static Long getLocalLastUpdated(){
        Long localLastUpdate = MyApplication.getContext().getSharedPreferences("TAG", Context.MODE_PRIVATE)
                .getLong("STUDENTS_LAST_UPDATE",0);
        return localLastUpdate;
    }

    static void setLocalLastUpdated(Long date){
        SharedPreferences.Editor editor = MyApplication.getContext()
                .getSharedPreferences("TAG", Context.MODE_PRIVATE).edit();
        editor.putLong("STUDENTS_LAST_UPDATE",date);
        editor.commit();
        Log.d("TAG", "new lud " + date);
    }
}
