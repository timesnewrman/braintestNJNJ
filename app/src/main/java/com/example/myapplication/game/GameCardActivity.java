package com.example.myapplication.game;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.FragmentManager;
import androidx.gridlayout.widget.GridLayout;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.data.DatabaseUpdater;
import com.example.myapplication.data.Level;
import com.example.myapplication.databinding.ActivityGameCardBinding;
import com.example.myapplication.ui.AlertDialogFragment;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

public class GameCardActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityGameCardBinding binding;
    private final Random rand = new Random();
    int gridSize;
    private boolean tutorial;

    private Bitmap[][] gridPattern;
    ArrayList <Bitmap> elements = new ArrayList<>();

    Bitmap brush;

    private final String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGameCardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        tutorial = Objects.equals(
                Objects.requireNonNull(getIntent().getExtras()).get("scenario"),
                Level.scenario.LAUNCH_TUTORIAL
        );

        gridSize = Math.max(
                getIntent().getIntExtra("difficulty",rand.nextInt(2)+2)/2%5,
                2);
        Log.i(TAG, String.valueOf(gridSize));

        int gridSizeintent = getIntent().getIntExtra("gridsize", 0);
        if (gridSizeintent!=0) gridSize = gridSizeintent;

        if (tutorial) gridSize = 1;

        Log.i(TAG, String.valueOf(gridSize)+" "+ String.valueOf(gridSizeintent));
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
                    if (tutorial) GameCardActivity.this.runOnUiThread(
                            () -> Toast.makeText(GameCardActivity.this
                                    ,"Remember the picture!"
                                    , Toast.LENGTH_SHORT).show()
                    );
                    Thread.sleep(3000L);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                displayPalette();
                vipeOut();
                GameCardActivity.this.runOnUiThread(
                        () -> {
                            binding.gameCardButton.setVisibility(View.VISIBLE);
                            if (tutorial) {
                                new AlertDialogFragment(
                                        "Choose the picture at the top of the screen...","OK"
                                ).show(GameCardActivity.this.getSupportFragmentManager(),"alerttutor_1");
                            }
                        });
            }

            private void vipeOut() {
                for (int i = 0; i < gridSize*gridSize; i++) {
                    ImageView gridElement = (ImageView) ((LinearLayout) GameCardActivity.this.binding.gameCardGrid.getChildAt(i)).getChildAt(0);
                    GameCardActivity.this.runOnUiThread(
                            () -> gridElement.setImageBitmap(elements.get(elements.size()-1)));
                }
            }

            private void displayPalette() {
                LinearLayout palette = binding.gameCardPallete;
                for (Bitmap fruit:elements) {

                    ImageButton imageButton = new ImageButton(GameCardActivity.this);

                    imageButton.setLayoutParams(new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    imageButton.setAdjustViewBounds(true);
                    imageButton.setScaleType(ImageView.ScaleType.FIT_XY);

                    imageButton.setImageBitmap(fruit);

                    GameCardActivity.this.runOnUiThread(
                            () -> palette.addView(imageButton));
                    imageButton.setOnClickListener(GameCardActivity.this);

                }
            }
        }




    private void initCards() {
        for (int i = 0; i<rand.nextInt();i++) {
            Integer drawable = Arrays.asList(
                    R.drawable.card_apple,
                    R.drawable.card_cherry,
                    R.drawable.card_graple,
                    R.drawable.card_lemon).get(i);
            elements.add(BitmapFactory.decodeResource(this.getResources(), drawable));

        }

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
                if (tutorial) new AlertDialogFragment(
                        "Then click on the card to set it to your selected fruit!", "OK"
                ).show(GameCardActivity.this.getSupportFragmentManager(),"alerttutor_2");
            }
            if (v == binding.gameCardButton){
                checkCard();
            }
        }

    private void checkCard() {
        binding.gameCardButton.setVisibility(View.INVISIBLE);
        int stars = 0;
        boolean pause = false;

        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                LinearLayout linearLayout = (LinearLayout) binding.gameCardGrid.getChildAt(i*gridSize+j);
                ImageView input = (ImageView) linearLayout.getChildAt(0);
                boolean isCardCorrect = ((BitmapDrawable)input.getDrawable()).getBitmap() == gridPattern[i][j];

                stars += isCardCorrect? 30 : -20;

                if (!isCardCorrect) {
                    input.setImageDrawable(
                            AppCompatResources.getDrawable(GameCardActivity.this, R.drawable.rectangle_17)
                    );
                    pause = true;
                }
            }
        }

        binding.gameCardStarcount.setText(String.valueOf(stars));

        DatabaseUpdater updater = new DatabaseUpdater(GameCardActivity.this);
        try {
            updater.increment(stars, getIntent());
            Context thisC = GameCardActivity.this;

            if (pause) Thread.sleep(1000);

            FragmentManager manager = getSupportFragmentManager();
            AlertDialogFragment dialog =
                    new AlertDialogFragment(
                            "You have earned "
                            +String.valueOf(stars)
                            +" stars out of "
                            +String.valueOf(30*gridSize*gridSize), "OK");

            dialog.ifSucsessful(()-> startActivity(new Intent(thisC, MainActivity.class)));
            dialog.show(manager, "myDialog");

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (RuntimeException e) {
            Log.e(TAG, String.valueOf(FirebaseAuth.getInstance().getCurrentUser()) + e.getCause());
        }
    }


}