package com.example.hobbiz;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.example.hobbiz.Model.DataModel;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

public class Login_page extends Fragment implements View.OnClickListener {
    private ProgressBar progressBar;
    private Button login_btn, signUp_btn;
    private EditText email, password;
    private View view;

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

        if (email_user.isEmpty()) {
            email.setError("Email is Required");
            email.requestFocus();
            return;
        }
        if (password_user.isEmpty()) {
            password.setError("Password is required");
            password.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email_user).matches()) {
            email.setError("Email is not valid");
            email.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        setEnabled(false);
        DataModel.data_instence.loginUser(email_user, password_user, new DataModel.LoginUserListener() {
            @Override
            public void onComplete(FirebaseUser user, Task<AuthResult> task) {
                if(task.isSuccessful()) {

                    SharedPreferences sp = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("email", email_user);
                    editor.putString("password", password_user);
                    editor.putString("userID", user.getUid());
                    editor.commit();

                    Navigation.findNavController(view).navigate(Login_pageDirections.actionLoginPageToHomePage2());
                }else {
                    setEnabled(true);
                    Toast.makeText(getActivity(), "Login Failed, email/password is not valid.", Toast.LENGTH_LONG).show();
                }
                progressBar.setVisibility(View.GONE);
            }
        });

    }

}