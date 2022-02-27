package com.example.hobbiz;

import android.content.Context;
import android.content.SharedPreferences;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.example.hobbiz.Model.Hobbiz;
import com.example.hobbiz.Model.Interfaces.OnItemClickListener;
import com.example.hobbiz.Model.Model;
import com.example.hobbiz.Model.Recycler.MyAdapter;
import com.example.hobbiz.Model.User;

import java.util.List;

public class Personal_Area_Details extends Fragment implements View.OnClickListener {

    private ImageButton addPost;
    private View view;
    private TextView name, email;
    private User user;
    private MyAdapter adapter;
    private RecyclerView recyclerView;
    private ProgressBar progressbar;
    private SwipeRefreshLayout swipeRefresh;
    private PersonalAreaViewModel viewModel;
    private Button logout;

    @Override
    public void onAttach(@NonNull Context context){
        super.onAttach(context);
        viewModel= new  ViewModelProvider(this).get(PersonalAreaViewModel.class);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_personal__area__details, container, false);
        SharedPreferences sp= getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        viewModel.setData(user);
        addPost = view.findViewById(R.id.add_post_btn);
        name = view.findViewById(R.id.userName_Tv2);
        email= view.findViewById(R.id.userEmail_Tv2);
        progressbar=view.findViewById(R.id.progressBar_in_personalArea);
        swipeRefresh=view.findViewById(R.id.hobby_swipe_refresh);
        logout=view.findViewById(R.id.logOut_from_personalArea);
        recyclerView=view.findViewById(R.id.hobby_list_on_personal);
        progressbar.setVisibility(View.VISIBLE);
        addPost.setOnClickListener(this);
        logout.setOnClickListener(this);

        adapter= new MyAdapter();
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(getContext());
        DividerItemDecoration dividerItemDecoration= new DividerItemDecoration(recyclerView.getContext(),linearLayoutManager.getOrientation());

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(dividerItemDecoration);


        viewModel.getData().observe(getViewLifecycleOwner(), new Observer<List<Hobbiz>>() {
            @Override
            public void onChanged(List<Hobbiz> products) {
                adapter.setFragment(Personal_Area_Details.this);
                adapter.setData(products);
                adapter.notifyDataSetChanged();
                progressbar.setVisibility(View.GONE);
            }
        });

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Hobbiz hobby = viewModel.getData().getValue().get(position);
                Home_PageDirections.ActionHomePageToHobbyDetailsPage action = Home_PageDirections.actionHomePageToHobbyDetailsPage(hobby);
                Navigation.findNavController(v).navigate(action);
            }
        });

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefresh.setRefreshing(true);
                Model.instance.reloadHobbysList();
                adapter.notifyDataSetChanged();
                swipeRefresh.setRefreshing(false);
            }
        });

        swipeRefresh.setRefreshing(Model.instance.getHobbizLoadingState().getValue() == Model.LoadingState.loading);

        Model.instance.getHobbizLoadingState().observe(getViewLifecycleOwner(), loadingState -> {
            swipeRefresh.setRefreshing(loadingState == Model.LoadingState.loading);
        });
        return view;

    }

    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.add_post_btn:
                Navigation.findNavController(view).navigate(Personal_Area_DetailsDirections.actionPersonalAreaDetailsToAddNewPost());
                break;

            case R.id.logOut_from_personalArea:
                SharedPreferences sp = getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
                SharedPreferences.Editor Ed=sp.edit();
                Ed.remove("email");
                Ed.remove("password");
                Ed.commit();

                Navigation.findNavController(view).navigate(Personal_Area_DetailsDirections.actionPersonalAreaDetailsToLoginPage());
        }
    }


    private void updateUserProfile() {
        name.setText(user.getFullName());
        progressbar.setVisibility(View.GONE);
    }
}