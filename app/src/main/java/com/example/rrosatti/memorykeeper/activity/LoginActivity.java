package com.example.rrosatti.memorykeeper.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
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
import com.example.rrosatti.memorykeeper.adapter.InternetAssync;
import com.example.rrosatti.memorykeeper.model.Email;
import com.example.rrosatti.memorykeeper.utils.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Properties;

import javax.mail.PasswordAuthentication;
import javax.mail.Session;

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
    private Email email = new Email();
    private final static String myEmail = "xxxxxxx";
    private final static String myPassword = "xxxxxxx";


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
                isLoading();

                auth.signInWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), getString(R.string.auth_failed) +
                                            task.getException(), Toast.LENGTH_SHORT).show();
                                    stopLoading();
                                } else {
                                    stopLoading();
                                    Intent inMemoryList = new Intent(LoginActivity.this, MemoryListActivity.class);
                                    inMemoryList.putExtra("userId", auth.getCurrentUser().getUid());
                                    startActivity(inMemoryList);
                                    finish();
                                }
                            }
                        });

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

        txtForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowAlertDialog();

            }
        });

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

    public void ShowAlertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle(getResources().getString(R.string.confirm));
        builder.setMessage("What email do you wanna receive a new password?");
        final EditText input = new EditText(this);
        builder.setView(input);
        builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(input.getText().toString().equals("")){
                    Toast.makeText(getBaseContext(),R.string.fields_should_not_be_empty,Toast.LENGTH_SHORT).show();
                }else{
                    isLoading();
                    auth.sendPasswordResetEmail(input.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this,
                                        R.string.email_sent, Toast.LENGTH_SHORT).show();
                                stopLoading();
                            } else {
                                Toast.makeText(LoginActivity.this,
                                        R.string.check_email,
                                        Toast.LENGTH_SHORT).show();
                                stopLoading();
                            }
                        }
                    });
                }
            }

        });
        builder.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.create();
        builder.show();
    }

    public void isLoading() {
        Util.isLoading(progressBar, LoginActivity.this);
    }

    public void stopLoading() {
        Util.stopLoading(progressBar, LoginActivity.this);
    }

}
