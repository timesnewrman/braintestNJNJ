package com.example.myapplication;

import static android.app.PendingIntent.getActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.myapplication.LoginActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.NavHostKt;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.widget.Button;
import android.widget.Toast;

import android.content.Intent;

import com.example.myapplication.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    FirebaseAuth fireAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        com.example.myapplication.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        fireAuth = FirebaseAuth.getInstance();

        checkAuthorisation();
        setContentView(binding.getRoot());


        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();

        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_activity_main);
        assert navHostFragment != null;
        NavController navController = navHostFragment.getNavController();

        NavigationUI.setupWithNavController(binding.navView, navController);

    }

    private void checkAuthorisation() {
        if (fireAuth.getCurrentUser() == null){
            Intent i = new Intent(MainActivity.this, LoginActivity.class)
                    .putExtra("user", true);
            startActivity(i);
        }
    }

    @Override
    protected  void onResume() {
        super.onResume();
        checkAuthorisation();
    }
    @Override
    public void onClick(View v) {
        Log.i("main activity", "recieved a click");
    }
//    @Override
//
//    public boolean onOptionsItemSelected(MenuItem item){
//        if(item.getItemId() == R.id.action_settings){
//
//        } else {
//            return super.onOptionsItemSelected(item);
//        }
//    }
}