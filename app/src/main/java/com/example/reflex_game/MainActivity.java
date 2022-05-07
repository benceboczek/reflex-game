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
                .requestIdToken("298873370929-4mkutaepkd6o4m2svp8t0k8e59cerfv5.apps.googleusercontent.com")
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    //ha a user be van jelentkezve akkor az onDestroy lifecycle-ben kijelentkezteti a rendszer
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mAuth.getCurrentUser() != null) {
            mAuth.signOut();
        }

    }

    //bejelentkezés megvalósítása
    public void play(View view) {
        String userName = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        //ha nincs meg a 4 karakter akkor egy toastot küldünk
        if(userName.length() < 4 || password.length() < 4){
            Toast.makeText(MainActivity.this, "Túl rövid!", Toast.LENGTH_LONG).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(userName, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override

            public void onComplete(@NonNull Task<AuthResult> task) {
                //ha sikeres az auth akkor intentet kapunk a GameActivity-re
                if (task.isSuccessful()) {
                    goToGame();
                //ha nem sikeres akkor alertet küldünk
                } else {
                    wrongPwAlert();
                }
            }

        });

    }

    //intent a játék gombra
    public void goToGame(){
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra ( "email", emailEditText.getText().toString() );
        startActivity(intent);
    }

    //felugró ablak megvalósítása
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


    //intent a regisztráció gombra
    public void register(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    //bejelentkezés regisztráció nélkül
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

    public EditText getEmail(){
        return this.emailEditText;
    }

}