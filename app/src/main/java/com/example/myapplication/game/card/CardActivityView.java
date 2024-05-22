package com.example.myapplication.game.card;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.gridlayout.widget.GridLayout;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.data.DatabaseUpdater;
import com.example.myapplication.databinding.ActivityGameCardBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class CardActivityView extends AppCompatActivity implements View.OnClickListener {

    ActivityGameCardBinding binding;
    private final Random rand = new Random();
    int gridSize;

    private Bitmap[][] gridPattern;
    ArrayList <Bitmap> elements = new ArrayList<>();

    Bitmap brush;

    private final String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGameCardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        gridSize = getIntent().getIntExtra("difficulty",rand.nextInt(2)+2);
        gridPattern = new Bitmap[gridSize][gridSize];
        initCards();
        initGrid();

        binding.gameCardButton.setOnClickListener(this);
        binding.gameCardButton.setVisibility(View.INVISIBLE);

        Log.i(TAG, Arrays.deepToString(gridPattern));

        startGame thread = new startGame();
        thread.start();
        }

        private class startGame extends Thread{
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(3000L);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                displayPalette();
                vipeOut();
                CardActivityView.this.runOnUiThread(
                        () -> binding.gameCardButton.setVisibility(View.VISIBLE));
            }

            private void vipeOut() {
                for (int i = 0; i < gridSize*gridSize; i++) {
                    ImageView gridElement = (ImageView) ((LinearLayout) CardActivityView.this.binding.gameCardGrid.getChildAt(i)).getChildAt(0);
                    CardActivityView.this.runOnUiThread(
                            () -> gridElement.setImageBitmap(elements.get(elements.size()-1)));
                }
            }

            private void displayPalette() {
                LinearLayout palette = binding.gameCardPallete;
                for (Bitmap fruit:elements) {

                    ImageButton imageButton = new ImageButton(CardActivityView.this);

                    imageButton.setLayoutParams(new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    imageButton.setAdjustViewBounds(true);
                    imageButton.setScaleType(ImageView.ScaleType.FIT_XY);

                    imageButton.setImageBitmap(fruit);

                    CardActivityView.this.runOnUiThread(
                            () -> palette.addView(imageButton));
                    imageButton.setOnClickListener(CardActivityView.this);

                }
            }
        }




    private void initCards() {
        elements.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.card_apple));
        elements.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.card_banana));
        elements.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.card_blueberries));
        elements.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.card_cherry));


        elements.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.card_back));
    }

    private void setCard(LinearLayout linearLayout, int i, int j) {
        int index = rand.nextInt(elements.size());

        gridPattern[i][j] = elements.get(index);
        linearLayout.setBackground(
                new BitmapDrawable( this.getResources(), elements.get(index) )
        );

        ImageView imageView = new ImageView(this);

        imageView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        imageView.setAdjustViewBounds(true);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        imageView.setOnClickListener(this);
        linearLayout.addView(imageView);
    }

    private void initGrid() {
        GridLayout grid = binding.gameCardGrid;


        grid.setRowCount(gridSize);
        grid.setColumnCount(gridSize);

        for (int i = 0; i < gridSize; i++) {
            GridLayout.Spec rowSpec = GridLayout.spec(i, 1,1);
            for (int j = 0; j < gridSize; j++) {
                GridLayout.Spec colSpec = GridLayout.spec(j, 1,1);

                LinearLayout linearLayout = new LinearLayout(this);

                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                linearLayout.setGravity(Gravity.FILL_HORIZONTAL);

                setCard(linearLayout, i, j);

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
            if (v.getClass() == ImageView.class && brush != null){
                ImageView newImage = (ImageView) v;
                newImage.setImageBitmap(brush);
            }
            if (v.getClass() == ImageButton.class){
                ImageButton view = (ImageButton) v;
                brush = ((BitmapDrawable)view.getDrawable()).getBitmap();
            }
            if (v == binding.gameCardButton){
                checkCard();
            }
        }

    private void checkCard() {
        binding.gameCardButton.setVisibility(View.INVISIBLE);
        int stars = 0;

        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                LinearLayout linearLayout = (LinearLayout) binding.gameCardGrid.getChildAt(i*gridSize+j);
                ImageView input = (ImageView) linearLayout.getChildAt(0);
                boolean isCardCorrect = ((BitmapDrawable)input.getDrawable()).getBitmap() == gridPattern[i][j];

                stars += isCardCorrect? 30 : -20;

                if (!isCardCorrect) {
                    input.setImageDrawable(
                            AppCompatResources.getDrawable(CardActivityView.this, R.drawable.rectangle_17)
                    );
                }
            }
        }

        binding.gameCardStarcount.setText(String.valueOf(stars));

        DatabaseUpdater updater = new DatabaseUpdater(CardActivityView.this);
        try {
            updater.increment(stars);
        }catch (RuntimeException e){
            Log.e(TAG, String.valueOf(FirebaseAuth.getInstance().getCurrentUser()) + e.getCause());
        }
        startActivity(new Intent(CardActivityView.this, MainActivity.class));
    }


}