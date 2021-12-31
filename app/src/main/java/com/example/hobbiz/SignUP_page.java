package com.example.hobbiz;

import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;


public class SignUP_page extends Fragment implements View.OnClickListener{
    View view;
    Button sign_up_btn;
    Button sign_in_btn;
    ProgressBar progressBar;
    EditText name_input, email_input, password_input;
    FirebaseAuth mAuth;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_sign_up_page, container, false);

        sign_up_btn = view.findViewById(R.id.sign_up_btn);
        sign_in_btn = view.findViewById(R.id.sign_in_btn_in_signUpPage);

        progressBar = view.findViewById(R.id.progressBar_in_signUP);

        name_input = view.findViewById(R.id.name_sign_up);
        email_input = view.findViewById(R.id.email_sign_up);
        password_input = view.findViewById(R.id.password_in_sign_up);

        sign_up_btn.setOnClickListener(this);
        sign_in_btn.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_up_btn:
                registerUser();
                break;
            case R.id.sign_in_btn_in_signUpPage:
                Navigation.findNavController(view).navigate(SignUP_pageDirections.actionSignUPPageToLoginPage2());
                break;
        }
    }

    private void setEnabled(boolean isEnabled) {
        sign_up_btn.setEnabled(isEnabled);
        sign_in_btn.setEnabled(isEnabled);
        name_input.setEnabled(isEnabled);
        email_input.setEnabled(isEnabled);
        password_input.setEnabled(isEnabled);
    }

    private void registerUser() {
        String userEmail = email_input.getText().toString();
        String userPassword = password_input.getText().toString();
        String userName = name_input.getText().toString();

        if(userEmail.isEmpty()) {
            email_input.setError("Required Field");
            email_input.requestFocus();
            return;
        }
        if(userPassword.isEmpty()) {
            password_input.setError("Required Field");
            password_input.requestFocus();
            return;
        }
        if(userName.isEmpty()) {
            name_input.setError("Required Field");
            name_input.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
            email_input.setError("Email not valid");
            email_input.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);


        mAuth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        User newUser = new User(userEmail,userName);
                        FirebaseDatabase.getInstance().getReference("users").setValue(newUser)
                                .addOnCompleteListener(task1 -> {
                                    if(task1.isSuccessful()) {
                                        Toast.makeText(getActivity(), "Sign-up success, email/password is not valid.", Toast.LENGTH_LONG).show();
                                        FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification();
                                        Navigation.findNavController(view).navigate(SignUP_pageDirections.actionSignUPPageToHomePage());
                                    } else {
                                        Toast.makeText(getActivity(), "Sign-up Failed, email/password is not valid.", Toast.LENGTH_LONG).show();
                                    }
                                    progressBar.setVisibility(View.GONE);
                                });
                    }
                });

    }


}