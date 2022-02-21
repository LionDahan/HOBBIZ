package com.example.hobbiz.Model.Recycler;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.hobbiz.Model.Interfaces.OnItemClickListener;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.hobbiz.R;

public class MyViewHolder extends RecyclerView.ViewHolder {
    TextView city, name, ages;
    ImageView img;
    Button moreInfoBtn;

    public MyViewHolder(@NonNull View itemView, OnItemClickListener listener) {
        super(itemView);
        city = itemView.findViewById(R.id.city_on_card);
        name = itemView.findViewById(R.id.name_on_card);
        ages = itemView.findViewById(R.id.age_on_card);
        img = itemView.findViewById(R.id.list_row_avatar);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void  onClick(View v) {
                int position = getAdapterPosition();
                if (listener != null) {
                    listener.onItemClick(position,v);
                }
            }
        });
    }


}