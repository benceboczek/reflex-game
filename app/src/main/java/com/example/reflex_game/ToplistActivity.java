package com.example.reflex_game;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ToplistActivity extends AppCompatActivity {
    private ListView highscoreListView;
    private FirebaseFirestore fStore;
    private ArrayList<String> arrayList;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toplist);
        getSupportActionBar().hide();
        highscoreListView = findViewById(R.id.highscoreListView);
        fStore = FirebaseFirestore.getInstance();
        arrayList = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(ToplistActivity.this, android.R.layout.simple_list_item_1, arrayList);
        highscoreListView.setAdapter(adapter);

        initializeData();

    }

    private void initializeData() {
        arrayList.clear();
        FirebaseFirestore.getInstance().collection("highscore").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot doc : task.getResult()){
                        arrayList.add(doc.getData().toString());
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    public void refresh(View view){
        initializeData();
    }

    public void goBack(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }
}