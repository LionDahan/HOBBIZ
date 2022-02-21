package com.example.hobbiz.Model;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.hobbiz.MyApplication;

import java.util.List;

public class Model {
    public static final Model instance = new Model();

    DataModel fbModel = new DataModel();
    MutableLiveData<List<Hobbiz>> hobbizList = new MutableLiveData<List<Hobbiz>>();
    MutableLiveData<LoadingState> hobbizListLoadingState= new MutableLiveData<LoadingState>();


    private Model(){
        hobbizListLoadingState.setValue(LoadingState.loaded);
        reloadHobbysList();

    }

    public enum LoadingState{
        loading,
        loaded
    }
    public LiveData<LoadingState> getHobbizLoadingState(){
        return hobbizListLoadingState;
    }

    public LiveData<List<Hobbiz>> getAllHobbiz(){return hobbizList;}


    public void reloadHobbysList() {
        hobbizListLoadingState.setValue(LoadingState.loading);
        Long localLastUpdate = Hobbiz.getLocalLastUpdated();
        Log.d("TAG","localLastUpdate: " + localLastUpdate);

        fbModel.getAllProducts(localLastUpdate,(list)->{
            MyApplication.executorService.execute(()->{
                Long lLastUpdate = new Long(0);
                Log.d("TAG", "FB returned " + list.size());
                for(Hobbiz hobbiz : list){
//                    AppLocalDB.db.studentDao().insertAll(product);
                    if (hobbiz.getLocalLastUpdated() > lLastUpdate){
                        lLastUpdate = hobbiz.getLocalLastUpdated();
                    }
                }
                Hobbiz.setLocalLastUpdated(lLastUpdate);

                //5. return all records to the caller
//                List<Product> stList = AppLocalDB.db.studentDao().getAll();
//                productListLtd.postValue(stList);
            });
        });
    }
}
