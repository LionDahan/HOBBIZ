package com.example.hobbiz.Model;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.hobbiz.Model.Interfaces.UploadHobbyListener;
import com.example.hobbiz.Model.Interfaces.UploadImageListener;
import com.example.hobbiz.MyApplication;
import com.google.android.gms.tasks.Task;

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

        fbModel.getAllHobbiz(localLastUpdate,(list)->{
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


            });
        }); fbModel.getAllHobbiz(localLastUpdate,(list)-> {
            if(list != null) {
                MyApplication.executorService.execute(()-> {
                    Long lastUpdate = new Long(0);
                    for(Hobbiz hobby : list) {
                        if(!hobby.isDelete_flag()) {
                            AppLocalDB.db.hobbizDao().insertAll(hobby);
                        }
                        else {
                            AppLocalDB.db.hobbizDao().delete(hobby);
                        }
                        if (hobby.getLastUpdated() > lastUpdate){
                            lastUpdate = hobby.getLastUpdated();
                        }
                    }

                    Hobbiz.setLocalLastUpdated(lastUpdate);
                    List<Hobbiz> petList = AppLocalDB.db.hobbizDao().getAll();

                    hobbizList.postValue(petList);
                    hobbizListLoadingState.postValue(LoadingState.loaded);
                });
            }
        });
    }
    public void uploadImage(Bitmap bitmap, String name, final UploadImageListener listener){
        fbModel.uploadImage(bitmap,name,listener);
    }

    //מה הסרט של זה????????????
    public void addHobby(Hobbiz hobby, Bitmap bitmap, UploadHobbyListener listener) {
        fbModel.uploadHobby(hobby, bitmap, new UploadHobbyListener() {
            @Override
            public void onComplete(Task task, Hobbiz hobby) {
                reloadHobbysList();
                listener.onComplete(task, hobby);
            }
        });
    }
}
