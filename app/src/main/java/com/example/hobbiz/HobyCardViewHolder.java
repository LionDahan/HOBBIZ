package com.example.hobbiz;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HobyCardViewHolder extends RecyclerView.ViewHolder {
    TextView name, city, age;
    ImageView imageView;
    public HobyCardViewHolder(@NonNull View itemView){
        super(itemView);

    }

}
