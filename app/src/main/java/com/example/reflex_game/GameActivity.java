package com.example.reflex_game;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class GameActivity extends AppCompatActivity {

    private boolean isGameStarted = false;
    private ImageView gameImage;
    private ImageView startImage;
    private int points = 0;
    private int lives = 3;
    private TextView pointTextView;
    private Handler handler;
    private long counter = 0;
    private int highscore = 0;
    private FirebaseFirestore db;
    private TextView scoreText;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        getSupportActionBar().hide();
        pointTextView = (TextView) findViewById(R.id.points);
        gameImage = (ImageView) findViewById(R.id.touch_icon);
        startImage = (ImageView) findViewById(R.id.play_icon);
        db = FirebaseFirestore.getInstance();
        handler = new Handler();
        scoreText = findViewById(R.id.points);
        gameManager();

    }



    public void gameTouch(View view) {
        points++;
        pointTextView.setText(String.valueOf(points));
        gameImage.setVisibility(View.GONE);

    }

    public void gameManager(){
        if(points <= 5) {
            gameTimer((int)(Math.random() * ((1000 - 700) + 1)) + 700);
        }
        else if(points > 5 && points <= 10){
            gameTimer((int)(Math.random() * ((800 - 600) + 1)) + 600);
        }
        else if(points > 10 && points <= 17){
            gameTimer((int)(Math.random() * ((700 - 400) + 1)) + 400);
        }
        else if(points > 17 && points <= 25){
            gameTimer((int)(Math.random() * ((600 - 300) + 1)) + 300);
        }
        else if(points > 25) {
            gameTimer((int)(Math.random() * ((500 - 200) + 1)) + 200);
        }

    }

    public void play(View view){
        isGameStarted = true;
        gameTouch(view);
        startImage.setVisibility(View.GONE);
        gameImage.setVisibility(View.GONE);
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
                    handler.postDelayed(this, milis);
                    randomizePosition();
                    if (points != counter) {
                        counter--;
                        lives -= 1;
                        animateScore();
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
        }, 2000);
    }

    public void updateDb(){
        long highscore = getHighscore();
        Map<String,Object> userHighscore = new HashMap<>();
        String email;
        try {
            email = getIntent().getExtras().getString("email");
        }catch (Exception e){
            email = "anonym";
        }


        if(email == null || email.equals("")) {
            userHighscore.put("email", "anonym");
        }
        else{
            userHighscore.put("email", email);
        }
        userHighscore.put("highscore", getPoints());

        db.collection("highscore")
                .add(userHighscore)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(GameActivity.this, "Rekord frissítve", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(GameActivity.this, "Nem sikerült frissíteni a rekordot", Toast.LENGTH_SHORT).show();

            }
        });

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
        updateDb();
    }

    public void animateScore(){
        Animation animation;
        animation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.simple_anim);
        scoreText.startAnimation(animation);
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