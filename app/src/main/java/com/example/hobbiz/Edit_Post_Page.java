package com.example.hobbiz;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.example.hobbiz.Model.Hobbiz;
import com.example.hobbiz.Model.Interfaces.DeleteHobbyListener;
import com.example.hobbiz.Model.Interfaces.EditHobbyListener;
import com.example.hobbiz.Model.Model;
import com.squareup.picasso.Picasso;


public class Edit_Post_Page extends Fragment implements View.OnClickListener{
    static final int REQUEST_IMAGE_CAPTURE = 1;
    View view;
    EditText description, name, age, contact, city;
    ImageButton takeImage, delete, save;
    Hobbiz hobby;
    ProgressBar progressBar;
    Bitmap bitmap = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hobby = Edit_Post_PageArgs.fromBundle(getArguments()).getHobby();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_edit__post__page, container, false);

        description = view.findViewById(R.id.description_in_edit);
        name = view.findViewById(R.id.ages_indetails);
        city = view.findViewById(R.id.ciryEditttt);
        age = view.findViewById(R.id.agesEditttttt);
        progressBar = view.findViewById(R.id.progresbar_in_edit_post);
        contact = view.findViewById(R.id.contactEdittt);
        takeImage = view.findViewById(R.id.image_in_edit_post);
        delete = view.findViewById(R.id.delete_in_edit_page);
        save = view.findViewById(R.id.save_in_edit_post);

        description.setText( hobby.getDescription());
        name.setText( hobby.getHobby_Name());
        age.setText(hobby.getCity());
        city.setText( hobby.getAge());
        contact.setText(hobby.getContact());

        Picasso.get().load(hobby.getImage()).into(takeImage);

        save.setOnClickListener(this);
        takeImage.setOnClickListener(this);
        delete.setOnClickListener(this);

        return view;
    }

    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.save_in_edit_post:
                saveEditedPost();
                break;
            case R.id.image_in_edit_post:
                uploadImage();
                break;
            case R.id.delete_in_edit_page:
                deletePost();
                break;
            default:
                break;
        }
    }

    public void deletePost() {
        progressBar.setVisibility(View.VISIBLE);
        Model.instance.deleteHobby(hobby, new DeleteHobbyListener() {
            @Override
            public void onComplete() {
                progressBar.setVisibility(View.INVISIBLE);
                Navigation.findNavController(view).navigate(Edit_Post_PageDirections.actionEditPostPageToHomePage());
            }
        });
    }

    public void saveEditedPost() {
        progressBar.setVisibility(View.VISIBLE);


        String descriptionIn, nameIn, cityIn, ageIn, contactIn;
        descriptionIn = description.getText().toString();
        nameIn = name.getText().toString();
        cityIn = city.getText().toString();
        ageIn = age.getText().toString();
        contactIn = contact.getText().toString();


        if (descriptionIn.isEmpty()){
            description.setError("Required Field");
            description.requestFocus();
            return; }

        if (nameIn.isEmpty()){
            name.setError("Required Field");
            name.requestFocus();
            return; }

        if (cityIn.isEmpty()){
            city.setError("Required Field");
            city.requestFocus();
            return; }

        if (ageIn.isEmpty()){
            age.setError("Required Field");
            age.requestFocus();
            return; }

        if (contactIn.isEmpty()){
            contact.setError("Required Field");
            contact.requestFocus();
            return; }

        hobby.setAge(ageIn);
        hobby.setDescription(descriptionIn);
        hobby.setCity(cityIn);
        hobby.setHobby_Name(nameIn);
        hobby.setContact(contactIn);

        Model.instance.editPost(hobby, bitmap, new EditHobbyListener() {
            @Override
            public void onComplete(Hobbiz hobbiz) {
                progressBar.setVisibility(View.INVISIBLE);
                Model.instance.reloadHobbysList();
                Navigation.findNavController(view).navigate(Edit_Post_PageDirections.actionEditPostPageToHomePage());
            }
        });

    }

    private void uploadImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,REQUEST_IMAGE_CAPTURE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            Bundle bundle = data.getExtras();
            bitmap = (Bitmap) bundle.get("data");
            takeImage.setImageBitmap(bitmap);
        }
    }
}