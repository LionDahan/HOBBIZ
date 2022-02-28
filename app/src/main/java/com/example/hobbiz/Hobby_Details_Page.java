package com.example.hobbiz;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;


import com.example.hobbiz.Model.Hobbiz;
import com.squareup.picasso.Picasso;


public class Hobby_Details_Page extends Fragment implements View.OnClickListener {
    private ImageButton toProfile;
    private View view;
    private Hobbiz hobby;
    private ProgressBar progressBar;
    private ImageView hobbyImage;
    private TextView name, city, ages, contact, description;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hobby = Hobby_Details_PageArgs.fromBundle(getArguments()).getHobby();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_hobby__details__page, container, false);

        toProfile = view.findViewById(R.id.personalA_in_hobby_details);
        name = view.findViewById(R.id.title_in_details_page);
        city = view.findViewById(R.id.age_in_add_post);
        ages = view.findViewById(R.id.name_in_add_post);
        contact = view.findViewById(R.id.city_in_add_post);
        description= view.findViewById(R.id.contact_in_add_post);
        hobbyImage = view.findViewById(R.id.image_in_add_post);
        progressBar = view.findViewById(R.id.progressBar_in_personalArea);


        toProfile.setOnClickListener(this);
        name.setText( hobby.getHobby_Name());
        city.setText( "City: " + hobby.getCity());
        ages.setText("Ages: "+hobby.getAge());
        contact.setText("Contact: "+ hobby.getContact());
        description.setText("Description: "+ hobby.getDescription());

        hobbyImage.setImageResource(R.drawable.tennis);
        if(hobby.getImage() != null) {
            Picasso.get().load(hobby.getImage()).into(hobbyImage);
        }
        return view;
    }

    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.personalA_in_hobby_details:
                Navigation.findNavController(view).navigate(Hobby_Details_PageDirections.actionHobbyDetailsPageToPersonalAreaDetails());
                break;

        }
    }


}