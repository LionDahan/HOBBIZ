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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login_page extends Fragment implements View.OnClickListener {
    ProgressBar progressBar;
    private FirebaseAuth mAuth;
    Button login_btn, signUp_btn;
    EditText email, password;
    View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_login_page, container, false);
        email = view.findViewById(R.id.email_sign_in);
        password = view.findViewById(R.id.password_sign_in);
        login_btn = view.findViewById(R.id.sign_in_btn_in_signin_p);
        signUp_btn = view.findViewById(R.id.sign_up_btn_in_LoginPage);
        progressBar= view.findViewById(R.id.progres_bar_in_sign_in);
        login_btn.setOnClickListener(this);
        signUp_btn.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.sign_in_btn_in_signin_p:
                login();
                break;
            case R.id.sign_up_btn_in_LoginPage:
                Navigation.findNavController(view).navigate(Login_pageDirections.actionLoginPageToSignUPPage());
                break;
        }
    }
    private void setEnabled(boolean is_enabled){
        email.setEnabled(is_enabled);
        password.setEnabled(is_enabled);
        signUp_btn.setEnabled(is_enabled);
        login_btn.setEnabled(is_enabled);
    }

    private void login() {
        String email_user = email.getText().toString().trim();
        String password_user = password.getText().toString().trim();

        if(email_user.isEmpty()) {
            email.setError("Email is Required");
            email.requestFocus();
            return;
        }
        if(password_user.isEmpty()) {
            password.setError("Password is required");
            password.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email_user).matches()) {
            email.setError("Email is not valid");
            email.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        setEnabled(false);
        mAuth.signInWithEmailAndPassword(email_user, password_user)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        progressBar.setVisibility(View.GONE);
                        if(task.isSuccessful()) {
                            if(user.isEmailVerified()) {
                                Navigation.findNavController(view).navigate(Login_pageDirections.actionLoginPageToHomePage2());
                                Toast.makeText(getActivity(), "Login successfully", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.INVISIBLE);
                            }else {
                                user.sendEmailVerification();
                                Toast.makeText(getActivity(), "Email Verification Sent, check your inbox! and try login again", Toast.LENGTH_SHORT).show();
                                Log.d("E", "Email verification.");
//                                pBar.setVisibility(View.INVISIBLE);
                                setEnabled(true);
                            }
                        }else {
                            Toast.makeText(getActivity(), "Login failed. Email/Password is incorrect", Toast.LENGTH_SHORT).show();
                            Log.d("ERR","login failed");
//                            pBar.setVisibility(View.INVISIBLE);
                            setEnabled(true);
                        }
                        Log.d("E", "finished siginin in.");
                    }
                });
        Navigation.findNavController(view).navigate(Login_pageDirections.actionLoginPageToHomePage2());
    }

}