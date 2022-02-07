package com.example.hobbiz.Model;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.hobbiz.MyApplication;

import java.util.List;

public class Model {
    public static final Model instance = new Model();
    DataModel fbModel = new DataModel();

    private Model(){
        reloadHobbysList();
    }

    public interface GetAllProductsListener {
        void onComplete(List<Hobbiz> data);
    }

    MutableLiveData<List<Hobbiz>> productListLtd = new MutableLiveData<List<Hobbiz>>();

    public void reloadHobbysList() {
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
