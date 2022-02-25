package com.example.hobbiz.Model;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.hobbiz.MyApplication;

@Database(entities = {Hobbiz.class, User.class}, version = 8)
abstract class AppLocalDbRepository extends RoomDatabase {
    public abstract HobbizDao hobbizDao();
    public abstract UserDao userDao();
}

public class AppLocalDB {
    static public final AppLocalDbRepository db =
            Room.databaseBuilder(MyApplication.getContext(),
                    AppLocalDbRepository.class,
                    "dbGetHobbiz.db")
                    .fallbackToDestructiveMigration()
                    .build();
    private AppLocalDB(){}
}
