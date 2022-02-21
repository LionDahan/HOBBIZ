package com.example.hobbiz.Model.Interfaces;

import com.example.hobbiz.Model.Hobbiz;
import com.google.android.gms.tasks.Task;

public interface UploadHobbyListener {
    void onComplete(Task task, Hobbiz hobby);
}
