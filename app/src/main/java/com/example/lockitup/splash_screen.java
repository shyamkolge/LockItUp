package com.example.lockitup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.lockitup.Model.Password;

public class splash_screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                    Intent iHome = new Intent(splash_screen.this,PatternLockAtc.class);
                    startActivity(iHome);
                    finish();

            }
        },5000);

    }
}