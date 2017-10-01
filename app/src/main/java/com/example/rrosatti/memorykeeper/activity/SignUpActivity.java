package com.example.rrosatti.memorykeeper.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.rrosatti.memorykeeper.R;

public class SignUpActivity extends AppCompatActivity {

    private EditText etName;
    private EditText etUsername;
    private EditText etPassword;
    private EditText etRepeatPassword;
    private Button btRegisterFingerprint;
    private Button btGenerateQRCode;
    private Button btCancel;
    private Button btOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        iniViews();

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void iniViews() {
        etName = (EditText) findViewById(R.id.activitySignUpEtName);
        etUsername = (EditText) findViewById(R.id.activitySignUpEtUsername);
        etPassword = (EditText) findViewById(R.id.activitySignUpEtPassword);
        etRepeatPassword = (EditText) findViewById(R.id.activitySignUpEtRepeatPassword);
        btRegisterFingerprint = (Button) findViewById(R.id.activitySignUpBtRegisterFingerprint);
        btGenerateQRCode = (Button) findViewById(R.id.activitySignUpBtGenerateQrCode);
        btCancel = (Button) findViewById(R.id.activitySignUpBtCancel);
        btOk = (Button) findViewById(R.id.activitySignUpBtOk);
    }
}
