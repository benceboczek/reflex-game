package com.example.reflex_game;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private static final String PREF_KEY = MainActivity.class.getPackage().toString();
    private static final String LOG_TAG = MainActivity.class.getName();


    EditText emailEditText;
    EditText passwordEditText;


    private SharedPreferences preferences;
    private GoogleSignInClient mGoogleSignInClient;


    private boolean anonym = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance();
        emailEditText = findViewById(R.id.editTextUserName);
        passwordEditText = findViewById(R.id.editTextPassword);
        preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }



    public void play(View view) {
        String userName = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if(userName.length() < 4 || password.length() < 4){
            Toast.makeText(MainActivity.this, "Túl rövid!", Toast.LENGTH_LONG).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(userName, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    goToGame();
                } else {
                    wrongPwAlert();
                }
            }

        });

    }

    public void goToGame(){
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }

    public void wrongPwAlert(){
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);

        dlgAlert.setMessage("Sikertelen bejelentkezés!");
        dlgAlert.setTitle("Hiba...");
        dlgAlert.setPositiveButton("OK", null);
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();

        dlgAlert.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
    }

    public void register(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void anonymRegister(View view) {
        mAuth.signInAnonymously().addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    anonym = true;
                    goToGame();
                } else {
                    wrongPwAlert();
                }
            }
        });

    }
}