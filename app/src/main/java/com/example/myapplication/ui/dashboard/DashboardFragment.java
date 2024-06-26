package com.example.myapplication.ui.dashboard;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.data.Level;
import com.example.myapplication.data.LevelAdapter;
import com.example.myapplication.databinding.FragmentDashboardBinding;

import java.util.ArrayList;

public class DashboardFragment
        extends Fragment
        implements View.OnClickListener,
                    View.OnScrollChangeListener
{

    private final String TAG = this.getClass().getSimpleName();
    private Button go_start;
    private final ArrayList<Level> list = new ArrayList<>();
    private SharedPreferences localData;

    RecyclerView levelView;

    private FragmentDashboardBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        localData = requireActivity().getSharedPreferences("level", Context.MODE_PRIVATE);

        go_start = binding.dashboardButtonstart;
        go_start.setOnClickListener(this);

        displayAdapterView();
        return binding.getRoot();
    }
    private void displayAdapterView(){

        levelView = binding.dashboardRecyclerview;

        Log.i(TAG, String.valueOf(localData.getInt("level", 666)));
        for (int i=0; i<localData.getInt("level", 1); i++) list.add(new Level(i));

        LevelAdapter levelAdapter = new LevelAdapter(getContext(), list, this);

        levelView.setLayoutManager(new GridLayoutManager(getContext(),2));
        levelView.setAdapter(levelAdapter);

        levelView.smoothScrollToPosition(list.size()-1);

        levelView.setOnScrollChangeListener(this);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View v) {
        if (v.getClass() == ConstraintLayout.class){
            ConstraintLayout button = (ConstraintLayout) v;
            int number = Integer.parseInt(((TextView) button.getChildAt(0)).getText().toString());
            Log.i(TAG, String.valueOf(number));

            Level.scenario scenario;
            scenario = (number == list.size()) ? Level.scenario.FROM_DASHBOARD: Level.scenario.NONE;
            if (number == 1) scenario = Level.scenario.LAUNCH_TUTORIAL;

            list.get(number-1).start(getContext(), scenario);
        }

        if (v == go_start) {
            levelView.smoothScrollToPosition(0);
            go_start.setVisibility(View.GONE);
        }
    }

    @Override
    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        go_start.setVisibility(oldScrollY > -1?
                View.VISIBLE:
                View.GONE);

        Log.i(TAG, String.valueOf(oldScrollY));
    }
}