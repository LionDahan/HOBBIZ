package com.example.hobbiz.Model;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import java.util.List;

@Dao
public interface HobbizDao {
    @Query("select * from Hobbiz")
    List<Hobbiz> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Hobbiz... hobbiz);

    @Delete
    void delete(Hobbiz hobby);

    @Query("SELECT * FROM Hobbiz WHERE id=:id ")
    Hobbiz getHobby(String id);

    @Query("SELECT * FROM Hobbiz WHERE userId=:id ")
    LiveData<List<Hobbiz>> getHobbyByUserId(String id);
}
