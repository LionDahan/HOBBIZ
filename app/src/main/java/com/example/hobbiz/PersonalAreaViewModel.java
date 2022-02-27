package com.example.hobbiz;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.hobbiz.Model.Hobbiz;
import com.example.hobbiz.Model.User;

import java.util.List;

public class PersonalAreaViewModel extends ViewModel {
    private LiveData<List<Hobbiz>> data;
    private User user;

    public LiveData<List<Hobbiz>> getData() {
        return data;
    }

    public void setData(User user) {
        this.user = user;


    }
}
