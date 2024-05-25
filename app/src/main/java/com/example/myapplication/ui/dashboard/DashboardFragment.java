package com.example.myapplication.ui.dashboard;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.data.Level;
import com.example.myapplication.data.LevelAdapter;
import com.example.myapplication.databinding.FragmentDashboardBinding;

import java.util.ArrayList;
import java.util.Objects;

public class DashboardFragment extends Fragment implements View.OnClickListener {

    private final String TAG = this.getClass().getSimpleName();
    private final ArrayList<Integer> list = new ArrayList<Integer>();
    private FragmentDashboardBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);


        displayAdapterView();
        return binding.getRoot();
    }
    private void displayAdapterView(){

        RecyclerView levelView = binding.dashboardRecyclerview;

        for (int i=0; i<111; i++) list.add(i);

        LevelAdapter levelAdapter = new LevelAdapter(getContext(), list, this);

        levelView.setLayoutManager(new GridLayoutManager(getContext(),2));
        levelView.setAdapter(levelAdapter);

        levelView.smoothScrollToPosition(list.size()-1);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View v) {
        Log.i(TAG, v.getClass()+" was clicked");
        //TODO finishing touches to the layout
        if (v.getClass() == ConstraintLayout.class){
            ConstraintLayout button = (ConstraintLayout) v;
            int seed = Integer.parseInt(((TextView) button.getChildAt(0)).getText().toString());
            int difficulty = Integer.parseInt(((TextView) button.getChildAt(1)).getText().toString());
            Level level = new Level(seed, difficulty);
            level.start(DashboardFragment.this.getContext());
        }

    }
}