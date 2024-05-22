package com.example.myapplication.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.LoginActivity;
import com.example.myapplication.game.card.CardActivityView;
import com.example.myapplication.game.GameMathActivity;
import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentHomeBinding;
import com.google.firebase.auth.FirebaseAuth;

public class HomeFragment extends Fragment implements View.OnClickListener {

    Button icard;
    Button imath, ipattern;
    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,

                             ViewGroup container, Bundle savedInstanceState){
        try{
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
//TODO change to main menu, make it so it contains your current level and stats

        icard = binding.mainGameintentCard;
        icard.setOnClickListener(this);
        imath = binding.mainGameintentFast;
        imath.setOnClickListener(this);
        ipattern = binding.mainLoginintentRegister;
        ipattern.setOnClickListener(this);


        return root;
        }catch (Exception e){
            Log.e("home","on create view", e);
            throw e;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View v) {
        if (v == icard){
            Intent i = new Intent(getActivity(), CardActivityView.class);
            startActivity(i);
        }
        if (v == imath){
            Intent i2 = new Intent(getActivity(), GameMathActivity.class);
            startActivity(i2);
        }
        if (v == ipattern){
            FirebaseAuth.getInstance().signOut();
            Intent i2 = new Intent(getActivity(), LoginActivity.class)
                    .putExtra("user", true);
            startActivity(i2);
        }
        if (v == binding.mainLoginintentSignin){
            FirebaseAuth.getInstance().signOut();
            Intent i2 = new Intent(getActivity(), LoginActivity.class)
                    .putExtra("user", false);
            startActivity(i2);
        }
    }
}