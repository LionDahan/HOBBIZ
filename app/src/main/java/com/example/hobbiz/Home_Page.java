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

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
;
import com.example.hobbiz.Model.Interfaces.GetUserById;
import com.example.hobbiz.Model.Interfaces.OnItemClickListener;
import com.example.hobbiz.Model.Model;
import com.example.hobbiz.Model.Hobbiz;
import com.example.hobbiz.Model.Recycler.MyAdapter;
import com.example.hobbiz.Model.User;

import java.util.List;


public class Home_Page extends Fragment implements View.OnClickListener{
    private ImageButton addPost, toProfile;
    private View view;
    private HomePageViewModel viewModel;
    private SwipeRefreshLayout swipeRefresh;
    private ProgressBar prbar;
    private RecyclerView hobiz_list;
    private MyAdapter adapter;
    private User curr_user;

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
        toProfile= view.findViewById(R.id.personalA_in_hobby_details);


        SharedPreferences sp = getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
        String userId = sp.getString("userID", null);
        if(userId!=null){
            Model.instance.getUserById(userId, new GetUserById() {
                @Override
                public void onComplete(User user) {
                    curr_user = user;
                }
            });

        }
        else{
            curr_user = new User();
        }
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
                Hobbiz hobby = viewModel.getData().getValue().get(position);
                Home_PageDirections.ActionHomePageToHobbyDetailsPage action =
                        Home_PageDirections.actionHomePageToHobbyDetailsPage(hobby);
                Navigation.findNavController(v).navigate(action);
            }
        });

        swipeRefresh.setRefreshing(Model.instance.getHobbizLoadingState().getValue()== Model.LoadingState.loading);

        Model.instance.getHobbizLoadingState().observe(getViewLifecycleOwner(), loadingState -> {
            swipeRefresh.setRefreshing(loadingState == Model.LoadingState.loading);
        });


        view.setFocusableInTouchMode(true);
        view.requestFocus();

        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction()== KeyEvent.ACTION_DOWN){
                    if(keyCode== KeyEvent.KEYCODE_BACK) {
                        return true;
                    }
                }
                return false;
            }
        });
        return view;

    }

    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.add_new_post_from_homepage1:
                Navigation.findNavController(view).navigate(Home_PageDirections.actionHomePageToAddNewPost());
                break;
            case R.id.personalA_in_hobby_details:
                Navigation.findNavController(view).navigate(Home_PageDirections.actionHomePageToPersonalAreaDetails2(curr_user));
                break;
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.hobbiz_list, menu);
    }


}