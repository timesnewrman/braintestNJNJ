package com.example.myapplication.ui.stats;
//TODO fix the adapter
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.DialogPreference;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.data.StatsAdapter;
import com.example.myapplication.data.UserStats;
import com.example.myapplication.databinding.FragmentStatsBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StatsFragment extends Fragment {
    private Exception exception;
    private FragmentStatsBinding binding;
    ArrayList<Object> databaseSave = new ArrayList<>();
    private final String TAG = this.getClass().getSimpleName();
    private final FirebaseFirestore fireStore = FirebaseFirestore.getInstance();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        StatsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(StatsViewModel.class);

        binding = FragmentStatsBinding.inflate(inflater, container, false);

        try {
            displayAdapterView();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return binding.getRoot();
    }

    @SuppressLint("SetTextI18n")
    private void displayAdapterView() throws Exception {
        RecyclerView statsView = binding.statsView;

        TextView errorView = binding.statsErrorText;

        fetchDatabase();

        Log.i(TAG, databaseSave.toString());

        StatsAdapter statsAdapter = new StatsAdapter(getActivity(), databaseSave);
        //TODO fix this method not getting past this comment
        Log.i(TAG, statsAdapter.toString()+" stats adapter created");

        statsView.setLayoutManager(new LinearLayoutManager(getActivity()));
        statsView.setAdapter(statsAdapter);

    }

    private void fetchDatabase(){
        fireStore.collection("users")
                .get()
                .addOnSuccessListener(task -> {
                    for (DocumentSnapshot document : task.getDocuments()){
                        databaseSave.add(new UserStats
                                        ((Objects.requireNonNull(document.getData())),
                                        this.getActivity())
                        );
                        Log.i(TAG, "added");
                    }
                })
                .addOnFailureListener(err -> {throw new RuntimeException(err);});

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}