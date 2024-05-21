package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.data.DatabaseUpdater;
import com.example.myapplication.data.UserStats;
import com.example.myapplication.databinding.ActivityLoginBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    boolean creatingUser;
    FirebaseAuth fireAuth = FirebaseAuth.getInstance();
    EditText emailView, passwordView, usernameView;
    Button loginButton;
    TextView errorView;
    ActivityLoginBinding binding;
    private final String TAG = this.getClass().getSimpleName();
    private final HashMap<String, String> errorExplanationText = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        creatingUser = Objects.requireNonNull(getIntent().getExtras()).getBoolean("user");

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initErrorMessages();
        initViews();

        loginButton.setText(creatingUser? "Register" : "Sign in");
    }

    private void initErrorMessages() {
        errorExplanationText.put("com.google.firebase.firestore.FirebaseFirestoreException",
                "Internal server error, try again later. Details: ");
        errorExplanationText.put("com.google.firebase.auth.FirebaseAuthInvalidCredentialsException",
                "Invalid credentials. ");
        errorExplanationText.put("com.google.firebase.auth.FirebaseAuthUserCollisionException",
                "User with this email already exists. ");
        errorExplanationText.put("com.google.firebase.FirebaseNetworkException",
                "Network error. Please check the internet connection. ");
    }

    private void initViews() {
        emailView = binding.loginEmail;
        passwordView = binding.loginPassword;
        usernameView = binding.loginUsername;
        errorView = binding.loginErrorHandlerText;

        loginButton = binding.loginButton;

        errorView.setVisibility(View.INVISIBLE);
        if(!creatingUser){
            usernameView.setVisibility(View.GONE);
        }

        loginButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == loginButton){
            String passwordText = String.valueOf(passwordView.getText());

            if (  passwordText.isEmpty()
               |  String.valueOf(emailView.getText()).isEmpty()

               |( String.valueOf(usernameView.getText()).isEmpty()
                   && usernameView.getVisibility()==View.VISIBLE )
                ){
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
        String username = String.valueOf(usernameView.getText());

        Log.i(TAG, email+password+username);
        if (creatingUser) {
            createUser(email, password, username);
        }  else {
            logIn(email, password);
        }
    }

    @SuppressLint("SetTextI18n")
    private void createUser(String email, String password, String username) {
        fireAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseFirestore database = FirebaseFirestore.getInstance();

                        DatabaseUpdater upload = new DatabaseUpdater(LoginActivity.this);
                        try {
                            upload.create(new UserStats(username));
                        }catch (RuntimeException e){
                            String causeClass = Objects.requireNonNull(e.getCause()).getClass().toString();
                            String explanation = errorExplanationText.get(causeClass);

                            showError(Objects.isNull(explanation) ? causeClass : explanation);
                        }

                    } else {
                        Exception error = task.getException();
                        assert error != null;

                        showError(error);
                    }
                });
    }

    @SuppressLint("SetTextI18n")
    private void logIn(String email, String password) {
        fireAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.wtf(TAG, "Authencated"+ Objects.requireNonNull(fireAuth.getCurrentUser()).getUid());
                        exitActivity(fireAuth.getCurrentUser());
                    } else {
                        Exception error = task.getException();
                        assert error != null;

                        showError(error);

                    }
                });
    }

    private void exitActivity(FirebaseUser user) {
        String username = String.valueOf(usernameView.getText());

        Intent i = new Intent(LoginActivity.this, MainActivity.class)
                .putExtra("user", user);
        binding = null;
        startActivity(i);


    }

    @SuppressLint("SetTextI18n")
    private void showError(String err){
        errorView.setVisibility(View.VISIBLE);
        errorView.setText(getString(R.string.login_failed)+err);
    }

    @SuppressLint("SetTextI18n")
    private void showError(Exception err){
        errorView.setVisibility(View.VISIBLE);
        errorView.setText(getString(R.string.login_failed)
                + errorExplanationText.get(Objects.requireNonNull(err.getClass().getName()))
                +err.toString());

        Log.e(TAG, Arrays.toString(err.getStackTrace()));
        Log.e(TAG, err.toString());
    }
}