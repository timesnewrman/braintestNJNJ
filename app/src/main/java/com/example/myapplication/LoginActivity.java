package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    FirebaseAuth fireAuth = FirebaseAuth.getInstance();
    EditText emailView, passwordView;
    Button loginView;
    TextView errorView;
    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_login);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initViews();
    }

    private void initViews() {
        emailView = binding.loginEmail;
        loginView = binding.loginButton;
        passwordView = binding.loginPassword;
        errorView = binding.loginErrorHandlerText;

        errorView.setVisibility(View.INVISIBLE);

        loginView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == loginView){
            String passwordText = String.valueOf(passwordView.getText());

            if (!(passwordView.getText() != null
               | emailView.getText() != null)){
                showError("Please fill in all fields.");
                return;
            }

            if (passwordText.length() < 7){
                showError("Password must be 8 or more in length.");
                return;
            }

            if (!String.valueOf(emailView.getText()).contains("@")){
                showError("Not valid email.");
                return;
            }

            authorize();
        }
    }

    private void authorize() {
        String email = String.valueOf(emailView.getText());
        String password = String.valueOf(passwordView.getText());

        if (Objects.requireNonNull(getIntent().getExtras()).getBoolean("user")) {
            logIn(email, password);
        }  else {
            createUser(email, password);
        }
    }

    @SuppressLint("SetTextI18n")
    private void createUser(String email, String password) {
        fireAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        exitActivity(fireAuth.getCurrentUser());
                    } else {
                        Exception error = task.getException();
                        assert error != null;

                        showError(error.toString());

                        Log.e("firebase auth", Arrays.toString(error.getStackTrace()));
                    }
                });
    }

    @SuppressLint("SetTextI18n")
    private void logIn(String email, String password) {
        fireAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        exitActivity(fireAuth.getCurrentUser());
                    } else {
                        Exception error = task.getException();
                        assert error != null;

                        showError(error.toString());

                        Log.e("firebase auth", Arrays.toString(error.getStackTrace()));
                    }
                });
    }

    private void exitActivity(FirebaseUser user) {
        Intent i = new Intent(this, MainActivity.class)
                .putExtra("user", user);
        binding = null;
        startActivity(i);
    }

    @SuppressLint("SetTextI18n")
    private void showError(String err){
        errorView.setVisibility(View.VISIBLE);
        errorView.setText(getString(R.string.login_failed)+err);
    }
}