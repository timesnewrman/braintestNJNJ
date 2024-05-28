package com.example.myapplication.ui.stats;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.data.StatsAdapter;
import com.example.myapplication.data.UserStats;
import com.example.myapplication.databinding.FragmentStatsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Objects;

public class StatsFragment extends Fragment {
    private FragmentStatsBinding binding;
    TextView errorView;
    ArrayList<Object> databaseSave = new ArrayList<>();
    private final String TAG = this.getClass().getSimpleName();
    StatsAdapter statsAdapter;
    String userId;
    private final FirebaseFirestore fireStore = FirebaseFirestore.getInstance();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

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

        fetchDatabase(()->{
            statsAdapter = new StatsAdapter(getActivity(), databaseSave, userId);

            statsView.setLayoutManager(new LinearLayoutManager(getActivity()));
            statsView.setAdapter(statsAdapter);

            errorView.setVisibility(View.INVISIBLE);

            int scrollTo = -1;
            for (Object obj:databaseSave){
                UserStats user = (UserStats) obj;
                if (Objects.equals(user.getUserid(), userId))
                    scrollTo = databaseSave.indexOf(obj);
            }
            if (scrollTo != -1) statsView.smoothScrollToPosition(scrollTo);
        });




    }

    private void fetchDatabase(Runnable runnable){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        userId = Objects.requireNonNull(auth.getCurrentUser()).getUid();

        fireStore.collection("users")
                .whereGreaterThan("stars", 0)
                .orderBy("stars", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(task -> {
                    for (DocumentSnapshot document : task.getDocuments()){
                        databaseSave.add(
                                new UserStats(Objects.requireNonNull(document.getData()))
                        );
                        Log.i(TAG, "added");
                    }
                    Log.i(TAG, databaseSave.toString());

                    runnable.run();
                })
                .addOnFailureListener(err -> {throw new RuntimeException(err);});

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}