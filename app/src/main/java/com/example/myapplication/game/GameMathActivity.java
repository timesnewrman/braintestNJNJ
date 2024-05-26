package com.example.myapplication.game;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.data.DatabaseUpdater;
import com.example.myapplication.databinding.ActivityGameMathBinding;
import com.example.myapplication.ui.AlertDialogFragment;

import java.util.ArrayList;
import java.util.Random;

public class GameMathActivity extends AppCompatActivity implements View.OnClickListener{
    private final String TAG = this.getClass().getSimpleName();
    ActivityGameMathBinding binding;
    Random rand = new Random();
    TextView questionText;
    TextView pointText;
    Button answerB1, answerB2, answerB3;
    ArrayList<Integer> elements = new ArrayList<>();
    int times;
    int total;
    int correct;
    int points;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGameMathBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        //setContentView(R.layout.activity_game_math);

        initViews();

        pointText.setText(String.valueOf(0));
        times = rand.nextInt(5)+2;
        total += times;

        answerB1.setOnClickListener(this);
        answerB2.setOnClickListener(this);
        answerB3.setOnClickListener(this);

        initQuestion(2);
        initButtons(rand);
    }

    private void initViews() {
        questionText = binding.mathQuestion;
        pointText = binding.mathStarcount;

        answerB1 = binding.mathButton;
        answerB2 = binding.mathButton2;
        answerB3 = binding.mathButton3;
    }

    private void initQuestion() {
        questionText.setText(toPresentableText(elements));
        correct = elements.stream().mapToInt(Integer::intValue).sum();
    }
    private void initQuestion(int add) {
        for (int i = 0; i < add; i++) {
            elements.add(
                    rand.nextInt(getIntent().getIntExtra("difficuly", 7)*3)
            );
        }

        questionText.setText(toPresentableText(elements));
        correct = elements.stream().mapToInt(Integer::intValue).sum();
    }

    @SuppressLint("SetTextI18n")
    private void initButtons(Random rand){
        //init buttons
        Button[] answerVariants = {answerB3, answerB2, answerB1};

        for (int i = answerVariants.length - 1; i > 0; i--)
        {
            int index = rand.nextInt(i + 1);
            Button a = answerVariants[index];
            answerVariants[index] = answerVariants[i];
            answerVariants[i] = a;
        }

        int i = 0;
        for (Button selectNames : answerVariants)
        {
            if (i == 0) {
                selectNames.setText(Integer.toString(correct));
                i++;
            } else {
                selectNames.setText(Integer.toString(correct + rand.nextInt(5)+1));
            }
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View v) {
        Log.d(TAG, "user clicked on activity here: "+ v.getClass());
        if (v == findViewById(R.id.math_button)
                || v == findViewById(R.id.math_button2)
                || v == findViewById(R.id.math_button3))
        {
            Button clickedButton = (Button) v;
            if (Integer.parseInt(clickedButton.getText().toString()) == correct)
            {
                points += 30;
                pointText.setText(Integer.toString(points));

                times--;
                if (times == 0)
                {
                    DatabaseUpdater updater = new DatabaseUpdater(GameMathActivity.this);
                    updater.increment(points, getIntent());

                    Context thisC = GameMathActivity.this;

                    FragmentManager manager = getSupportFragmentManager();
                    AlertDialogFragment dialog =
                            new AlertDialogFragment(
                                    "You have earned "
                                    +String.valueOf(points)
                                    + " stars out of "
                                    +String.valueOf(total*30), "OK");

                    dialog.ifSucsessful(() -> {
                        Log.i(TAG, "closed");
                        startActivity(new Intent(thisC, MainActivity.class));
                    });
                    dialog.show(manager, "myDialog");
                } else {
                    initQuestion(1);
                    initButtons(rand);
                }
            } else {
                points -= 20;
                pointText.setText(Integer.toString(points));

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                elements.remove(1);

                if (elements.size()<2) {initQuestion(1);} else {initQuestion();}

                initButtons(rand);
            }
        }
    }

    public static String toPresentableText(ArrayList<Integer> arr) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arr.size(); i++) {
            sb.append(arr.get(i));
            if (i < arr.size() - 1) {
                sb.append("+");
            }
        }
        return sb.toString();
    }
}