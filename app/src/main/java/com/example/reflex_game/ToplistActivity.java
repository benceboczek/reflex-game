package com.example.reflex_game;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class ToplistActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toplist);
        getSupportActionBar().hide();

    }
}