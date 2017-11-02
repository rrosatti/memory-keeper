package com.example.rrosatti.memorykeeper.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rrosatti.memorykeeper.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QRCodeLoginActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{

    private TextView txtQRCodeResult;
    private Button btReadQRCode;
    private ProgressBar progressBar;
    private ZXingScannerView mScannerView = null;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_login);

        iniViews();

        btReadQRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mScannerView = new ZXingScannerView(getApplicationContext());
                setContentView(mScannerView); /** initialize the scanner view*/
                mScannerView.setResultHandler(QRCodeLoginActivity.this); /** register as a handler for scan results*/
                mScannerView.startCamera();
            }
        });
    }

    private void iniViews() {
        txtQRCodeResult = (TextView) findViewById(R.id.txtQRCodeResult);
        btReadQRCode = (Button) findViewById(R.id.btReadQRCode);
        progressBar = (ProgressBar) findViewById(R.id.progressBarQRCodeLogin);
    }

    @Override
    public void handleResult(Result rawResult) {
        Log.e("handler", rawResult.getText());
        Log.e("handler", rawResult.getBarcodeFormat().toString());
        txtQRCodeResult.setText("Raw Result: " + rawResult.getText() +
                            "\nBarcode Format: " + rawResult.getBarcodeFormat().toString());
        mScannerView.resumeCameraPreview(QRCodeLoginActivity.this);
        //Toast.makeText(getApplicationContext(), "Raw Result: " + rawResult.getText(), Toast.LENGTH_SHORT).show();

        String[] res = rawResult.getText().split(";");

        if (res.length != 3) {
            Toast.makeText(getApplicationContext(), R.string.qrcode_invalid, Toast.LENGTH_SHORT).show();
        }

        mScannerView.stopCamera();
        progressBar.setVisibility(View.VISIBLE);

        auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword(res[1], res[2])
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    Intent in = new Intent(getApplicationContext(), MemoryListActivity.class);
                    in.putExtra("userId", auth.getCurrentUser().getUid());
                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // it will clear all "previous" activities
                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(in);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), R.string.auth_failed, Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });


        //Toast.makeText(getApplicationContext(), "Email: " + res[1], Toast.LENGTH_SHORT).show();
        //Log.d("LOOK HERE", "Email: " + res[1]);
        //mScannerView.stopCamera();

        // TODO:check if the given qr code is a "memory keeper qr code"
        /*if (true) {
            finish();
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.something_went_wrong),
                    Toast.LENGTH_SHORT).show();
        }*/

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mScannerView!=null)
            mScannerView.stopCamera();
    }
}
