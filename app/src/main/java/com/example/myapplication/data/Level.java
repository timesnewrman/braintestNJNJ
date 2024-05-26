package com.example.myapplication.data;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.example.myapplication.game.GameMathActivity;
import com.example.myapplication.game.GameCardActivity;

import java.util.Arrays;
import java.util.Random;

public class Level {
    int seed;
    int difficulty;
    Object activity;
    Random rand = new Random();

    public enum scenario{
        FROM_DASHBOARD,
        FROM_CHALLENGE,
        NONE;
    }
    public Level(int seed, int difficulty){
        this.seed = seed;
        this.difficulty = difficulty;
        rand.setSeed(seed);
        this.activity = Arrays.asList(
                GameCardActivity.class,
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
        this.difficulty=seed%20+3;
        this.activity = Arrays.asList(
                GameCardActivity.class,
                GameMathActivity.class
        ).get(seed%2);
    }


    public void start(Context context, scenario scenario) {

        Intent starter = new Intent(context, (Class<?>) activity);
         starter.putExtra("difficulty", difficulty)
                .putExtra("seed", seed)
                .putExtra("scenario", scenario);
        context.startActivity(starter);

    }

    @NonNull
    public String toString() {
        return "Level{" +
                "seed=" + seed +
                '}';
    }
}
