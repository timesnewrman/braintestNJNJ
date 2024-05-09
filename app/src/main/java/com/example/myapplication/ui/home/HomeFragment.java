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

import com.example.myapplication.GameCardActivity;
import com.example.myapplication.GameMathActivity;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentHomeBinding;

import java.util.Objects;

public class HomeFragment extends Fragment implements View.OnClickListener {

    Button icard;
    Button imath;
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

        icard = root.findViewById(R.id.main_gameintent_card);
        icard.setOnClickListener(this);
        imath = root.findViewById(R.id.main_gameintent_fast);
        imath.setOnClickListener(this);

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
        if (v==icard){
            Intent i = new Intent(getActivity(), GameCardActivity.class);
            startActivity(i);
        }
        if (v == imath){
            Intent i2 = new Intent(getActivity(), GameMathActivity.class);
            startActivity(i2);
        }
    }
}