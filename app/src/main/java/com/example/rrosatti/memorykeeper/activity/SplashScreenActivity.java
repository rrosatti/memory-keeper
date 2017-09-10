package com.example.rrosatti.memorykeeper.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.rrosatti.memorykeeper.R;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent in = new Intent(this, LoginActivity.class);
        startActivity(in);
        finish();
    }
}
