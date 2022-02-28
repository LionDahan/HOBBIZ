package com.example.hobbiz.Model;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.hobbiz.Model.Interfaces.DeleteHobbyListener;
import com.example.hobbiz.Model.Interfaces.EditHobbyListener;
import com.example.hobbiz.Model.Interfaces.GetUserById;
import com.example.hobbiz.Model.Interfaces.UploadHobbyListener;
import com.example.hobbiz.Model.Interfaces.UploadImageListener;
import com.example.hobbiz.MyApplication;
import com.google.android.gms.tasks.Task;

import java.util.List;

public class Model {
    public static final Model instance = new Model();

    DataModel fbModel = new DataModel();
    MutableLiveData<List<Hobbiz>> hobbizList = new MutableLiveData<>();
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

    public LiveData<List<Hobbiz>> getAllHobbiz(){
        return hobbizList;
    }

    public void reloadHobbysList() {
        hobbizListLoadingState.setValue(LoadingState.loading);
        Long localLastUpdate = Hobbiz.getLocalLastUpdated();

        fbModel.getAllHobbiz(localLastUpdate,(list)-> {
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
                        if (hobby.getLastUpdated() > lastUpdate) {
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

    public void addHobby(Hobbiz hobby, Bitmap bitmap, UploadHobbyListener listener) {
        fbModel.uploadHobby(hobby, bitmap, new UploadHobbyListener() {
            @Override
            public void onComplete(Task task, Hobbiz hobby) {
                reloadHobbysList();
                listener.onComplete(task, hobby);
            }
        });
    }
    public void getUserById(String userId, GetUserById listener) {
        fbModel.getUserById(userId,listener);
    }
    public LiveData<List<Hobbiz>> getUserHobbizByUserId(String userId){
        return AppLocalDB.db.hobbizDao().getHobbyByUserId(userId);
    }
    public void editPost(Hobbiz hobbiz, Bitmap bitmap, EditHobbyListener listener) {
        fbModel.editHobby(hobbiz, bitmap, listener);
    }

    public void deleteHobby(Hobbiz hobbiz, DeleteHobbyListener listener) {
        hobbiz.setDelete_flag(true);
        fbModel.deleteHobby(hobbiz, new DeleteHobbyListener() {
            @Override
            public void onComplete() {
                hobbiz.setDelete_flag(true);
                reloadHobbysList();
                listener.onComplete();
            }
        });
    }
}
