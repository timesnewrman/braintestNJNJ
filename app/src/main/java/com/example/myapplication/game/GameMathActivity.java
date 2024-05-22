package com.example.myapplication.game;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.data.DatabaseUpdater;
import com.example.myapplication.databinding.ActivityGameMathBinding;

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

        times = rand.nextInt(5)+2;

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
        questionText.setText(elements.toString());
        correct = elements.stream().mapToInt(Integer::intValue).sum();
    }
    private void initQuestion(int add) {
        for (int i = 0; i < add; i++) {
            elements.add(
                    rand.nextInt(getIntent().getIntExtra("difficuly", 7) * 5)
            );
        }

        questionText.setText(elements.toString());
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
                Toast.makeText(GameMathActivity.this,"correct", Toast.LENGTH_SHORT).show();
                points += 30;
                pointText.setText(Integer.toString(points));

                times--;
                if (times == 0)
                {
                    Toast.makeText(GameMathActivity.this, "congrats! you have "+ points +" points now", Toast.LENGTH_SHORT).show();

                    DatabaseUpdater updater = new DatabaseUpdater(GameMathActivity.this);
                    updater.increment(points);

                    startActivity(new Intent(GameMathActivity.this, MainActivity.class));
                } else {
                    initQuestion(1);
                    initButtons(rand);
                }
            } else {
                points -= 20;
                pointText.setText(Integer.toString(points));

                elements.remove(1);
                times++;
                initQuestion();

                initButtons(rand);
            }
        }
    }
}