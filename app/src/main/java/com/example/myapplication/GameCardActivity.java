package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import androidx.gridlayout.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.myapplication.databinding.ActivityGameCardBinding;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import java.util.ArrayList;
import java.util.stream.IntStream;

public class GameCardActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityGameCardBinding binding;
    private final Random rand = new Random();
    int gridSize = rand.nextInt(4);
    ArrayList <Bitmap> elements = new ArrayList<>();
    private final String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_card);

        initCards();
        initGrid();
        createPattern();
        }

    private void initCards() {
        elements.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.card_apple));
        elements.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.card_banana));
        elements.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.card_blueberries));
        elements.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.card_cherry));
        elements.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.card_back));
    }

    private void createPattern() {
        //TODO create pattern
    }

    private void initGrid() {
        GridLayout grid = findViewById(R.id.game_card_grid);


        grid.setRowCount(gridSize);
        grid.setColumnCount(gridSize);

        for (int i = 0; i < gridSize; i++) {
            GridLayout.Spec rowSpec = GridLayout.spec(i, 1,1);
            for (int j = 0; j < gridSize; j++) {
                GridLayout.Spec colSpec = GridLayout.spec(j, 1,1);

                LinearLayout linearLayout = new LinearLayout(this);

                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                linearLayout.setGravity(Gravity.FILL_HORIZONTAL);
                linearLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.card_back));

                ImageView imageView = new ImageView(this);
                imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                imageView.setAdjustViewBounds(true);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                imageView.setOnClickListener(this);
                linearLayout.addView(imageView);

                GridLayout.LayoutParams myGLP = new GridLayout.LayoutParams();
                myGLP.rowSpec = rowSpec;
                myGLP.columnSpec = colSpec;
                myGLP.width = 0;
                myGLP.height = 0;
                grid.addView(linearLayout, myGLP);
            }
        }
    }

    @Override
        public void onClick (View v){
            Log.d(TAG, v.getClass().toString());
            //TODO change logic of clicking
            if (v.getClass() == ImageView.class){
                ImageView newImage = (ImageView) v;
                AssetManager assetManager = getResources().getAssets();

                int index = rand.nextInt(elements.size());
                newImage.setImageBitmap(elements.get(index));
                newImage.setScaleType(ImageView.ScaleType.FIT_XY);
            }
        }
}