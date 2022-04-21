package com.example.reflex_game;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity {

    private boolean isGameStarted = false;
    private ImageView gameImage;
    private ImageView startImage;
    private int points = 0;
    private int lives = 3;
    private TextView pointTextView;
    private Runnable runnable;
    final int delay = 2000;
    private Handler handler;
    private long counter = 0;
    private int highscore = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        getSupportActionBar().hide();
        pointTextView = (TextView) findViewById(R.id.points);
        gameImage = (ImageView) findViewById(R.id.touch_icon);
        startImage = (ImageView) findViewById(R.id.play_icon);
        handler = new Handler();
        gameManager();

    }



    public void gameTouch(View view) {
        points++;
        pointTextView.setText(String.valueOf(points));
        gameImage.setVisibility(View.GONE);

    }

    public void gameManager(){
        gameTimer(delay);
    }

    public void play(View view){
        isGameStarted = true;
        gameTouch(view);
        startImage.setVisibility(View.GONE);
        gameImage.setVisibility(View.VISIBLE);

    }



    public void randomizePosition(){
        RelativeLayout.LayoutParams imagePositon = new RelativeLayout.LayoutParams(100, 100);
        imagePositon.leftMargin = (int)(Math.random() * ((900 - 100) + 1)) + 100;
        imagePositon.topMargin = (int)(Math.random() * ((1600 - 200) + 1)) + 200;
        gameImage.setVisibility(View.VISIBLE);
        gameImage.setLayoutParams(imagePositon);

    }

    public void goToRanklist(View view) {
        finish();
        Intent intent = new Intent(this, ToplistActivity.class);
        startActivity(intent);
    }

    public void gameTimer(long milis){
        handler.postDelayed(new Runnable() {
            public void run() {
                if(isGameStarted) {
                    counter++;
                    handler.postDelayed(this, delay);
                    randomizePosition();
                    if (points != counter) {
                        counter--;
                        lives -= 1;
                    }

                    Log.d("GameActivity", "points: " + points + "lives: " + lives + "counter: " + counter);
                    if (lives <= 0) {
                        manageDeath();
                        highscore = points;
                        counter = 0;
                        handler.removeCallbacksAndMessages(null);
                    }
                }
            }
        }, delay);
    }

    public void manageDeath(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setTitle("Meghaltál");

        alertDialogBuilder
                .setMessage("A pontszámod: " + getPoints())
                .setCancelable(false)
                .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        restartGame();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();
    }

    public void restartGame(){
        finish();
        Intent intent = new Intent(this, ToplistActivity.class);
        startActivity(intent);
    }



    public boolean isGameStarted() {
        return isGameStarted;
    }

    public int getPoints() {
        return points;
    }

    public int getLives() {
        return lives;
    }

    public long getCounter() {
        return counter;
    }

    public int getHighscore() {
        return highscore;
    }
}