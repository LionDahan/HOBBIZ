package com.example.hobbiz;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
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
import android.widget.TextView;

import com.example.hobbiz.Model.Model;
import com.example.hobbiz.Model.Hobbiz;
import com.squareup.picasso.Picasso;


public class Home_Page extends Fragment implements View.OnClickListener{
    ImageButton addPost, toProfile;
    View view;
    HomePageViewModel viewModel;
    SwipeRefreshLayout swipeRefresh;
    MyAdapter adapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home__page, container, false);

        RecyclerView list = view.findViewById(R.id.recycler_view);
        list.setHasFixedSize(true);

        list.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new MyAdapter();
        list.setAdapter(adapter);

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Hobbiz hobby = viewModel.getData().getValue().get(position);
                Log.d("TAG","row was clicked " + position);
//                homepage_FragmentDirections.ActionHomepageFragmentToGetDetailsFragment action = homepage_FragmentDirections.ActionHomepageFragmentToGetDetailsFragment(pet.getType());
//                Navigation.findNavController(v).navigate(action);
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

    interface OnItemClickListener{
        void onItemClick(int position, View v);
    }
    class MyAdapter extends RecyclerView.Adapter<MyViewHolder>{

        OnItemClickListener listener;
        public void setOnItemClickListener(OnItemClickListener listener){
            this.listener = listener;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.fragment_home__page,parent,false);
            MyViewHolder holder = new MyViewHolder(view,listener);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            Hobbiz hoby = viewModel.getData().getValue().get(position);
            holder.bind(hoby);
        }

        @Override
        public int getItemCount() {
            if (viewModel.getData().getValue() == null) return 0;
            return viewModel.getData().getValue().size();
        }
    }
}