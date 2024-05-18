package com.example.myapplication.game.card;

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
import androidx.gridlayout.widget.GridLayout;

import com.example.myapplication.R;
import com.example.myapplication.databinding.ActivityGameCardBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class CardActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityGameCardBinding binding;
    private final Random rand = new Random();
    int gridSize = rand.nextInt(2)+3;


    private final Bitmap[][] gridPattern = new Bitmap[gridSize][gridSize];
    ArrayList <Bitmap> elements = new ArrayList<>();



    private final String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGameCardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initCards();
        initGrid();

        Log.i(TAG, Arrays.deepToString(gridPattern));

        CardActivityModel thread = new CardActivityModel();
        thread.start();
        }

        private class CardActivityModel extends Thread{
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(200L);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                displayPalette();
                vipeOut();
            }

            private void vipeOut() {
                //TODO display card back for all grid buttons
            }

            private void displayPalette() {
                LinearLayout palette = binding.gameCardPallete;
                for (Bitmap fruit:elements) {

                    ImageButton imageButton = new ImageButton(CardActivity.this);

                    imageButton.setLayoutParams(new ViewGroup.LayoutParams(
                            60, ViewGroup.LayoutParams.MATCH_PARENT));
                    imageButton.setAdjustViewBounds(true);
                    imageButton.setScaleType(ImageView.ScaleType.FIT_XY);

                    imageButton.setImageBitmap(fruit);

                    palette.addView(imageButton);

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
            //TODO change logic of clicking;
            //TODO check for background if startGame thread gives permission; if user gets the card wrong take out the points
            if (v.getClass() == ImageView.class){
                ImageView newImage = (ImageView) v;

                int index = rand.nextInt(elements.size());
                newImage.setImageBitmap(elements.get(index));
                newImage.setScaleType(ImageView.ScaleType.FIT_XY);
            }
        }
}