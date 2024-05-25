package com.example.myapplication.data;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.game.GameMathActivity;
import com.example.myapplication.game.card.CardActivityView;

import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

public class Level {
    int seed;
    int difficulty;
    Object activity;
    Random rand = new Random();
    public Level(int seed, int difficulty){
        this.seed = seed;
        this.difficulty = difficulty;
        rand.setSeed(seed);
        this.activity = Arrays.asList(
                CardActivityView.class,
                GameMathActivity.class
        ).get(seed%2);
    }
    public Level(int seed, int difficulty, Object activity){
        this.seed = seed;
        this.difficulty = difficulty;
        this.activity = activity;
        rand.setSeed(seed);
    }

    public Level(int seed) {
        this.seed = seed;
        rand.setSeed(seed);
        this.activity = Arrays.asList(
                CardActivityView.class,
                GameMathActivity.class
        ).get(seed%2);
    }


    public void start(Context context) {

        Intent starter = new Intent(context, (Class<?>) activity);
         starter.putExtra("difficulty", rand.nextInt())
                .putExtra("seed", seed)
                .putExtra("levelClass", true);
        context.startActivity(starter);

    }

    @NonNull
    public String toString() {
        return "Level{" +
                "seed=" + seed +
                '}';
    }
}
