package com.example.rrosatti.memorykeeper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rrosatti.memorykeeper.R;
import com.example.rrosatti.memorykeeper.utils.EncryptPassword;
import com.example.rrosatti.memorykeeper.utils.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail;
    private EditText etPassword;
    private Button btLogin;
    private Button btSignUp;
    private ProgressBar progressBar;
    private TextView txtForgotPassword;
    private ImageButton btLoginWithFingerprint;
    private ImageButton btLoginWithQRCode;
    private DatabaseReference mDatabase;
    private DatabaseReference userDatabase;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        iniViews();

        auth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        userDatabase = mDatabase.child("users");

        /**if (auth.getCurrentUser() != null) {
            Intent in = new Intent(this, MemoryListActivity.class);
            startActivity(in);
            finish();
        }*/

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
                if (!checkInput()) {
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                Util.disableUserInteraction(LoginActivity.this);
                try{
                    EncryptPassword encryptPassword = new EncryptPassword();
                    String encryptedPass = encryptPassword.getEncryptedPass(etPassword.getText().toString());

                auth.signInWithEmailAndPassword(etEmail.getText().toString(), encryptedPass)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), getString(R.string.auth_failed) +
                                            task.getException(), Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                    Util.enableUserInteraction(LoginActivity.this);
                                } else {
                                    progressBar.setVisibility(View.GONE);
                                    Intent inMemoryList = new Intent(LoginActivity.this, MemoryListActivity.class);
                                    inMemoryList.putExtra("userId", auth.getCurrentUser().getUid());
                                    startActivity(inMemoryList);
                                    Util.enableUserInteraction(LoginActivity.this);
                                    finish();
                                }
                            }
                        });
                }catch (Exception ex){
                    ex.getMessage();
                }
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

        /*txtForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                SimpleEmail simpleEmail = new SimpleEmail();
                simpleEmail.addTo();
                simpleEmail.setFrom("ADMIM@ADMIN.COM");
                simpleEmail.setSubject("NEW PASSWORD");

                String text = "";
                String possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
                Random gerador = new Random();
                int numberRandom;
                for(int i = 0; i<8; i++){
                    numberRandom = gerador.nextInt(63);
                    text += possible.charAt(numberRandom);
                }

                simpleEmail.setMsg("Hello " + nome + "\n Your new password is: " + text);
                simpleEmail.send();
            }
        });*/

    }

    private void iniViews() {
        etEmail = (EditText) findViewById(R.id.activityLoginEtEmail);
        etPassword = (EditText) findViewById(R.id.activityLoginEtPassword);
        txtForgotPassword = (TextView) findViewById(R.id.txtForgotPass);
        btLogin = (Button) findViewById(R.id.btLogin);
        btSignUp = (Button) findViewById(R.id.btSignup);
        txtForgotPassword = (TextView) findViewById(R.id.txtForgotPass);
        btLoginWithFingerprint = (ImageButton) findViewById(R.id.btLoginWithFingerprint);
        btLoginWithQRCode = (ImageButton) findViewById(R.id.btLoginWithQrCode);
        progressBar = (ProgressBar) findViewById(R.id.progressBarLogin);
    }

    private boolean checkInput() {
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), getString(R.string.fill_all_fields),
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

}
