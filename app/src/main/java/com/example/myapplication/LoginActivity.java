package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.data.DatabaseUpdater;
import com.example.myapplication.data.UserStats;
import com.example.myapplication.databinding.ActivityLoginBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    boolean creatingUser;
    FirebaseAuth fireAuth = FirebaseAuth.getInstance();
    EditText emailView, passwordView, usernameView;
    Button loginButton, changeModeButton;
    TextView errorView, titleView;
    ActivityLoginBinding binding;
    private final String TAG = this.getClass().getSimpleName();
    private final HashMap<String, String> errorExplanationText = new HashMap<>();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        creatingUser = Objects.requireNonNull(getIntent().getExtras()).getBoolean("user");

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initErrorMessages();
        initViews();

        loginButton.setText(creatingUser?
                getString(R.string.action_register_short) : getString(R.string.action_sign_in_short));
        changeModeButton.setText(getString(R.string.or)+" "+ (!creatingUser?
                getString(R.string.action_register_short) : getString(R.string.action_sign_in_short))
        );
        titleView.setText("");
    }

    private void initErrorMessages() {
        errorExplanationText.put("com.google.firebase.firestore.FirebaseFirestoreException",
                getString(R.string.error_login_server));
        errorExplanationText.put("com.google.firebase.auth.FirebaseAuthInvalidCredentialsException",
                getString(R.string.error_login_invalidcredentials));
        errorExplanationText.put("com.google.firebase.auth.FirebaseAuthUserCollisionException",
                getString(R.string.error_login_alreadyexists));
        errorExplanationText.put("com.google.firebase.FirebaseNetworkException",
                getString(R.string.error_network));

    }

    @SuppressLint("SetTextI18n")
    private void initViews() {
        emailView = binding.loginEmail;
        passwordView = binding.loginPassword;
        usernameView = binding.loginUsername;
        errorView = binding.loginErrorHandlerText;
        titleView = binding.textView;

        loginButton = binding.loginButton;
        changeModeButton = binding.changeModeLogin;

        errorView.setVisibility(View.INVISIBLE);
        usernameView.setVisibility(creatingUser? View.VISIBLE:View.GONE);

        loginButton.setOnClickListener(this);
        changeModeButton.setOnClickListener(this);

        loginButton.setText(creatingUser?
                getString(R.string.action_register_short) : getString(R.string.action_sign_in_short));
        changeModeButton.setText(getString(R.string.or)+" "+ (!creatingUser?
                getString(R.string.action_register_short) : getString(R.string.action_sign_in_short))
        );
    }

    @Override
    public void onClick(View v) {
        if (v == loginButton){
            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
            String passwordText = String.valueOf(passwordView.getText());

            if (  passwordText.isEmpty()
               |  String.valueOf(emailView.getText()).isEmpty()

               |( String.valueOf(usernameView.getText()).isEmpty()
                   && usernameView.getVisibility()==View.VISIBLE )
                ){
                showError(getString(R.string.invalid_fields));
                return;
            }

            if (passwordText.length() < 7){
                showError(getString(R.string.invalid_password));
                return;
            }

            if (!String.valueOf(emailView.getText()).contains("@")){
                showError(getString(R.string.invalid_email));
                return;
            }

            authorize();
        }
        if (v==changeModeButton){
            creatingUser = !creatingUser;
            initViews();
            Log.i(TAG, "changed");
        }
    }

    private void authorize() {
        String email = String.valueOf(emailView.getText());
        String password = String.valueOf(passwordView.getText());
        String username = String.valueOf(usernameView.getText());

        loginButton.setVisibility(View.GONE);
        changeModeButton.setVisibility(View.GONE);

        Log.i(TAG, email+password+username);
        if (creatingUser) {
            createUser(email, password, username);
        }  else {
            logIn(email, password);
        }
    }

    @SuppressLint("SetTextI18n")
    private void createUser(String email, String password, String username) {
        titleView.setText("Registering you. Please wait");
        fireAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {

                        DatabaseUpdater upload = new DatabaseUpdater(LoginActivity.this);
                        try {
                            upload.create(new UserStats(
                                    username, Objects.requireNonNull(fireAuth.getCurrentUser()).getUid()
                            ));
                            titleView.setText("Done!");
                            exitActivity(fireAuth.getCurrentUser());
                        }catch (RuntimeException e){
                            showError(e);
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
        titleView.setText("Please wait");
        fireAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.i(TAG, "Authenticated"+ Objects.requireNonNull(fireAuth.getCurrentUser()).getUid());
                        titleView.setText("Done!");
                        exitActivity(fireAuth.getCurrentUser());
                    } else {
                        Exception error = task.getException();
                        assert error != null;

                        showError(error);

                    }
                });
    }

    private void exitActivity(FirebaseUser user) {
        Intent i = new Intent(LoginActivity.this, MainActivity.class)
                .putExtra("user", user);
        binding = null;
        startActivity(i);
    }

    @SuppressLint("SetTextI18n")
    private void showError(String err){
        errorView.setVisibility(View.VISIBLE);
        errorView.setText(getString(R.string.login_failed)+err);

        titleView.setText("");
        loginButton.setVisibility(View.VISIBLE);
        changeModeButton.setVisibility(View.VISIBLE);
    }

    @SuppressLint("SetTextI18n")
    private void showError(Exception err){
        errorView.setVisibility(View.VISIBLE);
        errorView.setText(getString(R.string.login_failed)
                + errorExplanationText.get(Objects.requireNonNull(err.getClass().getName()))
                + err);

        Log.e(TAG, Arrays.toString(err.getStackTrace()));
        Log.e(TAG, err.toString());

        loginButton.setVisibility(View.VISIBLE);
        changeModeButton.setVisibility(View.VISIBLE);
        titleView.setText("");
    }
}