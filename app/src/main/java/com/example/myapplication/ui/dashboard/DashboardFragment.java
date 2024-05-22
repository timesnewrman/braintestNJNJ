package com.example.myapplication.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.data.LevelAdapter;
import com.example.myapplication.databinding.FragmentDashboardBinding;

import java.util.Arrays;

public class DashboardFragment extends Fragment {

    private final String TAG = this.getClass().getSimpleName();
    private final Integer[] list = new Integer[5];
    private FragmentDashboardBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);

        //TODO course menu like duolingo
        displayAdapterView();
        return binding.getRoot();
    }
    private void displayAdapterView(){

        RecyclerView levelView = binding.dashboardRecyclerview;

        for (int i=0; i<5; i++){
            list[i] = i;
        }

        LevelAdapter levelAdapter = new LevelAdapter(getContext(), Arrays.asList(list));

        levelView.setLayoutManager(new GridLayoutManager(getContext(),2));
        levelView.setAdapter(levelAdapter);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}