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
        etName = (EditText) findViewById(R.id.activity_sign_up_et_name);
        etUsername = (EditText) findViewById(R.id.activity_sign_up_et_username);
        etPassword = (EditText) findViewById(R.id.activity_sign_up_et_password);
        etRepeatPassword = (EditText) findViewById(R.id.activity_sign_up_et_repeat_password);
        btRegisterFingerprint = (Button) findViewById(R.id.activity_sign_up_bt_register_fingerprint);
        btGenerateQRCode = (Button) findViewById(R.id.activity_sign_up_bt_generate_qr_code);
        btCancel = (Button) findViewById(R.id.activity_sign_up_bt_cancel);
        btOk = (Button) findViewById(R.id.activity_sign_up_bt_ok);
    }
}
