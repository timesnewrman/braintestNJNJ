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
    TextView errorView;
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
        } catch (RuntimeException e){
            errorView.setText(Objects.requireNonNull(e.getCause()).toString());
        }

        return binding.getRoot();
    }

    @SuppressLint("SetTextI18n")
    private void displayAdapterView(){

        RecyclerView statsView = binding.statsView;

        errorView = binding.statsErrorText;
        errorView.setText("Loading");
        errorView.setVisibility(View.VISIBLE);

        fetchDatabase(statsView);

        Log.i(TAG, databaseSave.toString());



    }

    private void fetchDatabase(RecyclerView statsView){
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
                    Log.i(TAG, databaseSave.toString());

                    StatsAdapter statsAdapter = new StatsAdapter(getActivity(), databaseSave);

                    statsView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    statsView.setAdapter(statsAdapter);

                    errorView.setVisibility(View.INVISIBLE);
                })
                .addOnFailureListener(err -> {throw new RuntimeException(err);});

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}