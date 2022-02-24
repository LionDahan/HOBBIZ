package com.example.hobbiz;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.example.hobbiz.Model.Recycler.MyAdapter;
import com.example.hobbiz.Model.User;

public class Personal_Area_Details extends Fragment implements View.OnClickListener {

    ImageButton addPost;
    View view;
    EditText FullName;
    User user;
    MyAdapter adapter;
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
        user = .fromBundle(getArguments()).getUser();

        viewModel.setData(user);
        addPost = view.findViewById(R.id.add_post_btn);
        FullName = view.findViewById(R.id.edit_name_txt);
        progressbar=view.findViewById(R.id.progressBar_in_personalArea);
        swipeRefresh=view.findViewById(R.id.hobby_swipe_refresh);
        logout=view.findViewById(R.id.logOut_from_personalArea);

        addPost.setOnClickListener(this);
        logout.setOnClickListener(this);
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
        FullName.setText(user.getFullName());
        progressbar.setVisibility(View.GONE);

    }
}