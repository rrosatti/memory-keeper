package com.example.rrosatti.memorykeeper.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rrosatti.memorykeeper.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.File;
import java.io.FileOutputStream;

public class SignUpActivity extends AppCompatActivity {

    private EditText etName;
    private EditText etUsername;
    private EditText etPassword;
    private EditText etRepeatPassword;
    private Button btRegisterFingerprint;
    private Button btGenerateQRCode;
    private Button btCancel;
    private Button btOk;
    private DatabaseReference mDatabase;
    private DatabaseReference userDatabase;
    private int PERMISSION_SDCARD = 0;
    private String pathQrCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        iniViews();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        userDatabase = mDatabase.child("users");

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btGenerateQRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!checkUserInput()) return;

                // check if app is running on Android Marshmallow
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    if (!checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) return;

                /**
                 * Code based on the following StackOverflow links:
                 *
                 * https://stackoverflow.com/questions/8800919/how-to-generate-a-qr-code-for-an-android-application
                 * &&
                 * https://stackoverflow.com/questions/21085682/how-to-save-an-image-from-imageview
                 */
                // convert content in QRCode
                QRCodeWriter writer = new QRCodeWriter();
                try {
                    String username = etUsername.getText().toString();
                    String password = etPassword.getText().toString();
                    String qrCodeContent = "memory-keeper;" + username + ";" + password + ";";
                    BitMatrix bitMatrix = writer.encode(qrCodeContent,
                                                    BarcodeFormat.QR_CODE, 512, 512);
                    int width = bitMatrix.getWidth();
                    int height = bitMatrix.getHeight();
                    Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
                    // generate BitMap image
                    for (int x = 0; x < width; x++) {
                        for (int y = 0; y < height; y++) {
                            bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                        }
                    }

                    // save the qr code in the user's phone
                    saveQRCode(bmp);

                } catch (Exception e) {
                    e.printStackTrace();
                }
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

    private void saveQRCode(Bitmap bmp) {
        // save imageView in gallery
        String cameraPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString();
        File cachePath = new File(cameraPath + "/memory-keeper-qr-code.jpg");
        pathQrCode = cameraPath.toString();
        System.out.println("Path: " + pathQrCode);
        try {
            cachePath.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(cachePath);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean checkUserInput() {
        String name = etName.getText().toString();
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();
        String repeatPassword = etRepeatPassword.getText().toString();

        if (name.equals("") || username.equals("") || password.equals("") || repeatPassword.equals("")) {
            Toast.makeText(getApplicationContext(), "You must feel all fields", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!password.equals(repeatPassword)) {
            Toast.makeText(getApplicationContext(),
                    "The value of RepeatPassword does not match the Password", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean checkPermission(String permission) {
        // check if device has the permission
        if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            // sent permission request
            ActivityCompat.requestPermissions(this, new String[]{permission}, PERMISSION_SDCARD);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // check permission code
        if (requestCode == PERMISSION_SDCARD) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), "Permission Granted!!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Permission Denied!!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
