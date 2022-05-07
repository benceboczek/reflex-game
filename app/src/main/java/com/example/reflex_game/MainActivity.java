package com.example.reflex_game;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
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

    EditText emailEditText;
    EditText passwordEditText;
    TextView gameText;
    AlarmManager mAlarmManager;

    private SharedPreferences preferences;
    private GoogleSignInClient mGoogleSignInClient;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        mAuth = FirebaseAuth.getInstance();
        emailEditText = findViewById(R.id.editTextUserName);
        passwordEditText = findViewById(R.id.editTextPassword);
        preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);
        gameText = findViewById(R.id.gameText);
        mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("298873370929-4mkutaepkd6o4m2svp8t0k8e59cerfv5.apps.googleusercontent.com")
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        permissionRequest();
        //setAlarmManager();
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
            Toast.makeText(MainActivity.this, "Túl rövid azonosítók!", Toast.LENGTH_LONG).show();
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
                    goToGame();
                } else {
                    wrongPwAlert();
                }
            }
        });

    }

    public void permissionRequest(){
            if (ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION)){
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                }else{
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                }
            }

        }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(MainActivity.this,
                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {}
                }
                return;
            }
        }
    }

    //animáció megvalósítása
    public void animateGameText(View view) {
        Animation animation;
        animation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.simple_anim);
        gameText.startAnimation(animation);
    }

    private void setAlarmManager() {
        long repeatInterval = AlarmManager.INTERVAL_DAY;
        long triggerTime = SystemClock.elapsedRealtime() + repeatInterval;

        Intent intent = new Intent(this, RecieveScheduler.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        mAlarmManager.setInexactRepeating(
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                triggerTime,
                repeatInterval,
                pendingIntent);


        mAlarmManager.cancel(pendingIntent);
    }

    public EditText getEmail(){
        return this.emailEditText;
    }


}