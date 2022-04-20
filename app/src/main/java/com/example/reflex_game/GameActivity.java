package com.example.reflex_game;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Random;

public class GameActivity extends AppCompatActivity {

    private boolean isGameStarted = false;
    private ImageView gameImage;
    private int points = -1;
    private int lives = 3;
    private TextView pointTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        getSupportActionBar().hide();
        pointTextView = (TextView) findViewById(R.id.points);
    }


    public void gameTouch(View view) {
        randomizePosition();
        points ++;
        pointTextView.setText(String.valueOf(points));
    }

    public void randomizePosition(){
        gameImage = (ImageView) findViewById(R.id.touch_icon);


        RelativeLayout.LayoutParams imagePositon = new RelativeLayout.LayoutParams(100, 100);
        imagePositon.leftMargin = (int)(Math.random() * ((900 - 100) + 1)) + 100;
        imagePositon.topMargin = (int)(Math.random() * ((1600 - 200) + 1)) + 200;

        gameImage.setLayoutParams(imagePositon);

    }

    public void goToRanklist(View view) {
        Intent intent = new Intent(this, ToplistActivity.class);
        startActivity(intent);
    }



}