package com.example.hobbiz.Model.Recycler;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hobbiz.Model.Hobbiz;
import com.example.hobbiz.Model.Interfaces.OnItemClickListener;
import com.example.hobbiz.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
    OnItemClickListener listener;
    private List<Hobbiz> data;
    private Fragment fragment;

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = fragment.getLayoutInflater().inflate(R.layout.fragment_hoby_card,parent,false);
        MyViewHolder holder = new MyViewHolder(view,listener);
        return holder;
    }

    public void setData(List <Hobbiz> data) {
        this.data = data;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    @Override
    public int getItemCount() {
        if(data==null)
            return 0;
        return data.size();
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Hobbiz hobbiz = data.get(position);
        holder.name.setText(hobbiz.getHobby_Name());
        holder.city.setText(hobbiz.getCity());
        holder.ages.setText(hobbiz.getAge());

        if (hobbiz.getImage() != null) {
            Picasso.get().load(hobbiz.getImage()).into(holder.img);
        }
    }
}