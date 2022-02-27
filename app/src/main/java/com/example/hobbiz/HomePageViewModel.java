package com.example.hobbiz;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.example.hobbiz.Model.Hobbiz;
import com.example.hobbiz.Model.Model;
import java.util.List;
//להמשיך פה חסר

public class HomePageViewModel extends ViewModel {
    LiveData<List<Hobbiz>> data = Model.instance.getAllHobbiz();
    
    public LiveData<List<Hobbiz>>getData() {
        return data;
    }

}
