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

import com.example.myapplication.data.UserData;
import com.example.myapplication.databinding.ActivityLoginBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    boolean creatingUser;
    FirebaseAuth fireAuth = FirebaseAuth.getInstance();
    EditText emailView, passwordView, usernameView;
    Button loginView;
    TextView errorView;
    ActivityLoginBinding binding;
    private final String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        creatingUser = Objects.requireNonNull(getIntent().getExtras()).getBoolean("user");

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initViews();
    }

    private void initViews() {
        emailView = binding.loginEmail;
        passwordView = binding.loginPassword;
        usernameView = binding.loginUsername;
        errorView = binding.loginErrorHandlerText;

        loginView = binding.loginButton;

        errorView.setVisibility(View.INVISIBLE);
        if(!creatingUser){
            usernameView.setVisibility(View.GONE);
        }

        loginView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == loginView){
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

        if (creatingUser) {
            createUser(email, password);
        }  else {
            logIn(email, password);
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

                        showError(error);

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

                        showError(error);

                    }
                });
    }

    private void exitActivity(FirebaseUser user) {
        String username = String.valueOf(usernameView.getText());

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection("users")
                .document(Objects.requireNonNull(fireAuth.getCurrentUser()).getUid())
                .set(new UserData(username))
                .addOnFailureListener(e -> {
                    showError(e.toString());

                })
                .addOnSuccessListener(voidd -> {
                    Intent i = new Intent(LoginActivity.this, MainActivity.class)
                            .putExtra("user", user);
                    binding = null;
                    startActivity(i);
                });

    }

    @SuppressLint("SetTextI18n")
    private void showError(String err){
        errorView.setVisibility(View.VISIBLE);
        errorView.setText(getString(R.string.login_failed)+err);
    }

    @SuppressLint("SetTextI18n")
    private void showError(Exception err){
        errorView.setVisibility(View.VISIBLE);
        errorView.setText(getString(R.string.login_failed)+err);
        Log.e(TAG, Arrays.toString(err.getStackTrace()));
    }
}