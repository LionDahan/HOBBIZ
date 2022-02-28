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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.hobbiz.Model.Constants;
import com.example.hobbiz.Model.Hobbiz;
import com.example.hobbiz.Model.Interfaces.GetUserById;
import com.example.hobbiz.Model.Interfaces.OnItemClickListener;
import com.example.hobbiz.Model.Model;
import com.example.hobbiz.Model.Recycler.MyAdapter;
import com.example.hobbiz.Model.User;

import org.w3c.dom.Text;

import java.util.List;

public class Personal_Area_Details extends Fragment implements View.OnClickListener {

    ImageButton addPost;
    View view;
    TextView email;
    MyAdapter adapter;
    RecyclerView recyclerView;
    ProgressBar progressbar;
    SwipeRefreshLayout swipeRefresh;
    PersonalAreaViewModel viewModel;
    Button logout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel =  new ViewModelProvider(this).get(PersonalAreaViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_personal__area__details, container, false);


        addPost = view.findViewById(R.id.add_post_btn);
        email= view.findViewById(R.id.profile_email);
        progressbar=view.findViewById(R.id.progressBar_in_personalArea);
        swipeRefresh=view.findViewById(R.id.hobby_swipe_refresh_personal);
        logout=view.findViewById(R.id.logOut_from_personalArea);
        recyclerView=view.findViewById(R.id.hobby_list_on_personal);
        progressbar.setVisibility(View.VISIBLE);

        addPost.setOnClickListener(this);
        logout.setOnClickListener(this);

        SharedPreferences sp = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        String userId = sp.getString("userId", null);
        String emailText = sp.getString("email", null);
        viewModel.setData(userId);

        adapter= new MyAdapter();
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(getContext());
        DividerItemDecoration dividerItemDecoration= new DividerItemDecoration(recyclerView.getContext(),linearLayoutManager.getOrientation());

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(dividerItemDecoration);

        email.setText(emailText);

        viewModel.getData().observe(getViewLifecycleOwner(), posts -> {
            adapter.setFragment(Personal_Area_Details.this);
            adapter.setData(posts);
            adapter.notifyDataSetChanged();
            progressbar.setVisibility(View.INVISIBLE);
        });

        adapter.setOnItemClickListener((position, v) -> {
            Hobbiz hobby = viewModel.getData().getValue().get(position);
            Personal_Area_DetailsDirections.ActionPersonalAreaDetailsToEditPostPage action =
                    Personal_Area_DetailsDirections.actionPersonalAreaDetailsToEditPostPage(hobby);
            Navigation.findNavController(v).navigate(action);
        });

        swipeRefresh.setOnRefreshListener(() -> {
            swipeRefresh.setRefreshing(true);
            Model.instance.reloadHobbysList();
            adapter.notifyDataSetChanged();
            swipeRefresh.setRefreshing(false);
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
                SharedPreferences sp = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
                SharedPreferences.Editor Ed=sp.edit();
                Ed.remove("email");
                Ed.remove("password");
                Ed.remove("userId");
                Ed.commit();

                Navigation.findNavController(view).navigate(Personal_Area_DetailsDirections.actionPersonalAreaDetailsToLoginPage());

        }
    }


}