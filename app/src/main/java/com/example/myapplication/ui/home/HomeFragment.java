package com.example.myapplication.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.myapplication.LoginActivity;
import com.example.myapplication.R;
import com.example.myapplication.data.Level;
import com.example.myapplication.databinding.FragmentHomeBinding;
import com.example.myapplication.game.GameMathActivity;
import com.example.myapplication.game.GameCardActivity;
import com.example.myapplication.ui.AlertDialogFragment;
import com.google.firebase.auth.FirebaseAuth;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class HomeFragment extends Fragment implements View.OnClickListener {

    Button icard;
    Button imath, ilogin;

    SharedPreferences localData;
    int currentDate;
    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,

                             ViewGroup container, Bundle savedInstanceState){
        try{

            binding = FragmentHomeBinding.inflate(inflater, container, false);
            View root = binding.getRoot();

            localData = requireContext().getSharedPreferences("level", Context.MODE_PRIVATE);

            initButtons();
            initChallenge();

            return root;
        }catch (Exception e){
            Log.e("home","on create view", e);
            throw e;
        }
    }

    private void initChallenge() {
        int dateData = localData.getInt("date", -1);
        LocalDate date = LocalDate.now();

        currentDate = Integer.parseInt(date.format(DateTimeFormatter.ofPattern("D")));
        binding.totalstarchallenge.setText(String.valueOf(Level.calculateStars(currentDate)));

        if (dateData != currentDate) {
            binding.challengeOfDay.setOnClickListener(this);
        } else {
            binding.challengeOfDay.setCardBackgroundColor(requireContext().getColor(R.color.card_background));
            binding.homeCardtitle1.setTextColor(requireContext().getColor(R.color.dark));
            binding.challengeAccept.setVisibility(View.GONE);
            binding.homeCardtitle1.setText(R.string.challenge_completed);
        }
    }

    private void initButtons() {
        icard = binding.mainGameintentCard;
        icard.setOnClickListener(this);
        imath = binding.mainGameintentFast;
        imath.setOnClickListener(this);
        ilogin = binding.mainLoginintentRegister;
        ilogin.setOnClickListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View v) {
        int difficulty = binding.difficultyBar.getProgress();
        Log.i("diff", String.valueOf(difficulty));
        if (v == icard){
            Intent i = new Intent(getActivity(), GameCardActivity.class)
                    .putExtra("scenario", Level.scenario.NO_STARS)
                    .putExtra("gridsize", (difficulty/2)+2);
            startActivity(i);
        }
        if (v == imath){
            Intent i2 = new Intent(getActivity(), GameMathActivity.class)
                    .putExtra("scenario", Level.scenario.NO_STARS)
                    .putExtra("difficulty", difficulty*2);
            startActivity(i2);
        }
        if (v == ilogin){
            AlertDialogFragment dialog = new AlertDialogFragment(getString(R.string.log_out_confirmation), "Yes", "No");
            dialog.ifSucsessful(()->{
                FirebaseAuth.getInstance().signOut();
                Intent i2 = new Intent(getActivity(), LoginActivity.class)
                        .putExtra("user", true);
                startActivity(i2);
            });
            dialog.show(requireActivity().getSupportFragmentManager(), "loginDialog");
        }
        if (v == binding.challengeOfDay){
            localData.edit().putInt("date", currentDate).apply();
            Level level = new Level(currentDate);
            level.start(this.getContext(), Level.scenario.FROM_CHALLENGE);
        }
    }
}