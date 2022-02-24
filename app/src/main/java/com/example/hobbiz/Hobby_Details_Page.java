package com.example.hobbiz;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;


import com.example.hobbiz.Model.User;
import com.example.hobbiz.Model.Hobbiz;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;


public class Hobby_Details_Page extends Fragment implements View.OnClickListener {
    ImageButton toProfile;
    View view;
    Hobbiz hobby;
    ProgressBar progressBar;
    ImageView hobbyImage;
    TextView name, city, ages, contact, description;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hobby = getDetails_FragmentArgs.fromBundle(getArguments()).getPet();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_hobby__details__page, container, false);

        toProfile = view.findViewById(R.id.personalA_in_hobby_details);
        name = view.findViewById(R.id.title_in_details_page);
        city = view.findViewById(R.id.city_in_details_page);
        ages = view.findViewById(R.id.ages_in_details_page);
        contact = view.findViewById(R.id.contact_in_details_page);
        description= view.findViewById(R.id.description_in_details_page);
        hobbyImage = view.findViewById(R.id.image_in_details_page);
        progressBar = view.findViewById(R.id.progressBar_in_personalArea);

        toProfile.setOnClickListener(this);
        name.setText( hobby.getHobby_Name()+ "Class");
        city.setText("City: " + hobby.getCity());
        ages.setText("Ages: " + hobby.getAge());
        contact.setText("Contact: " + hobby.getContact());
        description.setText("Description: " + hobby.getDescription());

        hobbyImage.setImageResource(R.drawable.tennis);
        Log.d("",hobby.getImage());
        if(hobby.getImage() != null) {
            Picasso.get().load(hobby.getImage()).into(hobbyImage);
        }
        return view;
    }

    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.personalA_in_hobby_details:
                Navigation.findNavController(view).navigate(Hobby_Details_PageDirections.actionHobbyDetailsPageToPersonalAreaDetails(new User()));
                break;


        }
    }


}