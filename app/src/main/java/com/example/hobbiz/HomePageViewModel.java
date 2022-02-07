package com.example.hobbiz;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.hobbiz.Model.Hobbiz;

import java.util.List;
//להמשיך פה חסר 

public class HomePageViewModel extends ViewModel {
    LiveData<List<Hobbiz>> data;
    
    public LiveData<List<Hobbiz>>getData(){
        return data;
        //
    }

}
