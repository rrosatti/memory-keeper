package com.example.rrosatti.memorykeeper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.rrosatti.memorykeeper.R;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername;
    private EditText etPassword;
    private Button btLogin;
    private Button btSignUp;
    private TextView txtForgotPassword;
    private ImageButton btLoginWithFingerprint;
    private ImageButton btLoginWithQRCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        iniViews();

        btSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inSignUp = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(inSignUp);
            }
        });

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inMemoryList = new Intent(LoginActivity.this, MemoryListActivity.class);
                startActivity(inMemoryList);
            }
        });

        btLoginWithQRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getApplicationContext(), QRCodeLoginActivity.class);
                startActivity(in);
            }
        });

        btLoginWithFingerprint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getApplicationContext(), FingerprintLoginActivity.class);
                startActivity(in);
            }
        });

    }

    private void iniViews() {
        etUsername = (EditText) findViewById(R.id.activityLoginEtUsername);
        etPassword = (EditText) findViewById(R.id.activityLoginEtPassword);
        btLogin = (Button) findViewById(R.id.btLogin);
        btSignUp = (Button) findViewById(R.id.btSignup);
        txtForgotPassword = (TextView) findViewById(R.id.txtForgotPass);
        btLoginWithFingerprint = (ImageButton) findViewById(R.id.btLoginWithFingerprint);
        btLoginWithQRCode = (ImageButton) findViewById(R.id.btLoginWithQrCode);
    }

}
