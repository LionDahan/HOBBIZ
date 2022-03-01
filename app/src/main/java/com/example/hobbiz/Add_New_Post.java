package com.example.hobbiz;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import static android.app.Activity.RESULT_OK;


import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.hobbiz.Model.DataModel;
import com.example.hobbiz.Model.Hobbiz;
import com.example.hobbiz.Model.Interfaces.UploadHobbyListener;
import com.google.android.gms.tasks.Task;


public class Add_New_Post extends Fragment implements View.OnClickListener {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private ImageButton save, cancel, add_pic;
    private EditText city, ages, name, contact, description;
    private ProgressBar progressBar;
    private View view;
    private Bitmap bitmap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_add__new__post, container, false);
        cancel = view.findViewById(R.id.cancel_btn_inadd_post);
        save= view.findViewById(R.id.save_in_add_post);
        name= view.findViewById(R.id.ages_indetails);
        ages =view.findViewById(R.id.agesEditttttt);
        city =view.findViewById(R.id.ciryEditttt);
        contact = view.findViewById(R.id.contactEdittt);
        description = view.findViewById(R.id.description_in_edit);
        progressBar= view.findViewById(R.id.progresbar_in_add_post);
        add_pic = view.findViewById(R.id.image_in_add_post);

        cancel.setOnClickListener(this);
        save.setOnClickListener(this);
        add_pic.setOnClickListener(this);


        return view;
    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel_btn_inadd_post:
                Navigation.findNavController(view).navigate(Add_New_PostDirections.actionAddNewPostToHomePage());
                break;
            case R.id.image_in_add_post:
                uploadImage();
                break;
            case R.id.save_in_add_post:
                savePost();
                break;
        }
    }
    private void savePost(){
        String hobbyName_input, age_input, city_input, contact_input, description_input;
        hobbyName_input = name.getText().toString();
        age_input = ages.getText().toString();
        city_input = city.getText().toString();
        contact_input = contact.getText().toString();
        description_input = description.getText().toString();

        if (hobbyName_input.isEmpty()){
            name.setError("Required Field");
            name.requestFocus();
            return; }

        if (age_input.isEmpty()){
            ages.setError("Required Field");
            ages.requestFocus();
            return; }

        if (city_input.isEmpty()){
            city.setError("Required Field");
            city.requestFocus();
            return; }

        if (contact_input.isEmpty()){
            contact.setError("Required Field");
            contact.requestFocus();
            return; }

        if (description_input.isEmpty()){
            description.setError("Required Field");
            description.requestFocus();
            return; }

        if (bitmap == null){ return;}

        progressBar.setVisibility(View.VISIBLE);

        Hobbiz hobby = new Hobbiz(hobbyName_input,age_input,city_input, contact_input,description_input);
        SharedPreferences sp= getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        String userId = sp.getString("userId", null);
        hobby.setUserId(userId);
        DataModel.data_instence.uploadHobby(hobby, bitmap, new UploadHobbyListener() {
            @Override
            public void onComplete(Task task, Hobbiz hobby) {
                if (hobby.getImage() != null){
                    Toast.makeText(getActivity(), "Upload Success", Toast.LENGTH_LONG).show();
                    Navigation.findNavController(view).navigate(Add_New_PostDirections.actionAddNewPostToHomePage());
                } else{
                    Toast.makeText(getActivity(),"Upload Failed", Toast.LENGTH_LONG).show();
                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }


    private void uploadImage(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,REQUEST_IMAGE_CAPTURE);


    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            Bundle bundle = data.getExtras();
            bitmap = (Bitmap) bundle.get("data");
            add_pic.setImageBitmap(bitmap);
        }
    }

}
