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

    }

    private void iniViews() {
        etUsername = (EditText) findViewById(R.id.activity_login_et_username);
        etPassword = (EditText) findViewById(R.id.activity_login_et_password);
        btLogin = (Button) findViewById(R.id.bt_login);
        btSignUp = (Button) findViewById(R.id.bt_signup);
        txtForgotPassword = (TextView) findViewById(R.id.txt_forgot_pass);
        btLoginWithFingerprint = (ImageButton) findViewById(R.id.bt_login_with_fingerprint);
        btLoginWithQRCode = (ImageButton) findViewById(R.id.bt_login_with_qr_code);
    }

}
