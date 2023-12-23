package com.ramish.spotit;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreen extends AppCompatActivity {

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        mAuth = FirebaseAuth.getInstance();

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {

                if(mAuth.getCurrentUser() != null) {

                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }

                else {

                    startActivity(new Intent(getApplicationContext(), Login.class));
                }

                finish();
            }
        }, 5000);
    }
}