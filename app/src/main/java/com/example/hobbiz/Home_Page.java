package com.example.hobbiz;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.hobbiz.Model.Interfaces.OnItemClickListener;
import com.example.hobbiz.Model.Model;
import com.example.hobbiz.Model.Hobbiz;
import com.example.hobbiz.Model.Recycler.MyAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;


public class Home_Page extends Fragment implements View.OnClickListener{
    ImageButton addPost, toProfile;
    View view;
    HomePageViewModel viewModel;
    SwipeRefreshLayout swipeRefresh;
    ProgressBar prbar;
    RecyclerView hobiz_list;
    MyAdapter adapter;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(HomePageViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home__page, container, false);
        hobiz_list = view.findViewById(R.id.recycler_view);
        prbar= view.findViewById(R.id.prBar_homepage);
        swipeRefresh= view.findViewById(R.id.hobby_list_swipe_refresh);
        addPost= view.findViewById(R.id.add_new_post_from_homepage1);
        toProfile= view.findViewById(R.id.personalArea2);
        addPost.setOnClickListener(this);
        toProfile.setOnClickListener(this);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefresh.setRefreshing(true);
                Model.instance.reloadHobbysList();
                adapter.notifyDataSetChanged();
                swipeRefresh.setRefreshing(false);
            }
        });
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(getContext());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(hobiz_list.getContext(), linearLayoutManager.getOrientation());
        adapter= new MyAdapter();
        hobiz_list.setHasFixedSize(true);
        hobiz_list.setAdapter(adapter);
        hobiz_list.setLayoutManager(linearLayoutManager);
        hobiz_list.addItemDecoration(dividerItemDecoration);
        setHasOptionsMenu(true);
        viewModel.getData().observe(getViewLifecycleOwner(), new Observer<List<Hobbiz>>() {
            @Override
            public void onChanged(List<Hobbiz> hobbizs) {
                adapter.setFragment(Home_Page.this);
                adapter.setData(hobbizs);
                adapter.notifyDataSetChanged();
                prbar.setVisibility(View.GONE);

            }
        });
        prbar.setVisibility(View.VISIBLE);


        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Hobbiz hoby= viewModel.getData().getValue().get(position);
                Home_PageDirections.

            }

        });

        swipeRefresh = view.findViewById(R.id.hobby_list_swipe_refresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Model.instance.reloadHobbysList();
            }
        });

        setHasOptionsMenu(true);
//        if (viewModel.getData() == null ) {refreshData();};
//
//        viewModel.getData().observe(getViewLifecycleOwner(), (studentsList)->{
//            adapter.notifyDataSetChanged();
//        });

//        swipeRefresh.setRefreshing(Model.instance.getStudentListLoadingState().getValue() == Model.LoadingState.loading);
//        Model.instance.getStudentListLoadingState().observe(getViewLifecycleOwner(), loadingState -> {
//            swipeRefresh.setRefreshing(loadingState == Model.LoadingState.loading);
//        });
        return view;
    }

    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.add_new_post_from_homepage1:
                Navigation.findNavController(view).navigate(Home_PageDirections.actionHomePageToAddNewPost());
                break;
            case R.id.personalArea2:
                Navigation.findNavController(view).navigate(Home_PageDirections.actionHomePageToPersonalAreaDetails2());
                break;
        }
    }

    private void refreshData() {

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        //לסדרinflater.inflate(R.menu.pet_list ,menu);
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView city, name, age;
        ImageView img;
        Button moreInfoBtn;

        public MyViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            name = itemView.findViewById(R.id.name_on_card);
            city = itemView.findViewById(R.id.city_on_card);
            age = itemView.findViewById(R.id.age_on_card);
            moreInfoBtn = itemView.findViewById(R.id.imageButton4);
            img = itemView.findViewById(R.id.list_row_avatar);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (listener != null) {
                        listener.onItemClick(pos,v);
                    }
                }
            });
        }

        public void bind(Hobbiz hoby){
            city.setText(hoby.getCity());
            age.setText(hoby.getAge());
            name.setText(hoby.getHobby_Name());
            String url = hoby.getImage().toString();
            if (url != null && !url.equals("")) {
                Picasso.get()
                        .load(url)
                        .placeholder(R.drawable.tennis)
                        .into(img);
            }
        }
    }

    interface OnItemClickListener {
        void onItemClick(int position, View v);
    }
}