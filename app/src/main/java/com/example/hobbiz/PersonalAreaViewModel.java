package com.example.hobbiz;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.hobbiz.Model.Hobbiz;
import com.example.hobbiz.Model.Model;
import com.example.hobbiz.Model.User;

import java.util.List;

public class PersonalAreaViewModel extends ViewModel {
    private LiveData<List<Hobbiz>> data;
    private String userId;

    public LiveData<List<Hobbiz>> getData() {
        return data;
    }

    public void setData(String userId) {
        this.userId = userId;
        this.data = Model.instance.getUserHobbizByUserId(userId);


    }
}
