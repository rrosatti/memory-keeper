package com.example.rrosatti.memorykeeper.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rrosatti.memorykeeper.R;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QRCodeLoginActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{

    private TextView txtQRCodeResult;
    private Button btReadQRCode;
    private ZXingScannerView mScannerView;

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
    }

    @Override
    public void handleResult(Result rawResult) {
        Log.e("handler", rawResult.getText());
        Log.e("handler", rawResult.getBarcodeFormat().toString());
        txtQRCodeResult.setText("Raw Result: " + rawResult.getText() +
                            "\nBarcode Format: " + rawResult.getBarcodeFormat().toString());
        mScannerView.resumeCameraPreview(QRCodeLoginActivity.this);
        mScannerView.stopCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }
}