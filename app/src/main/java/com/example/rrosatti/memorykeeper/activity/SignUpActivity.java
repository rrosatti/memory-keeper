package com.example.rrosatti.memorykeeper.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.rrosatti.memorykeeper.R;
import com.example.rrosatti.memorykeeper.adapter.InternetAsync;
import com.example.rrosatti.memorykeeper.model.Email;
import com.example.rrosatti.memorykeeper.model.User;
import com.example.rrosatti.memorykeeper.utils.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Properties;

import javax.mail.PasswordAuthentication;
import javax.mail.Session;

public class SignUpActivity extends AppCompatActivity {

    private EditText etName;
    private EditText etUsername;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etRepeatPassword;
    private Button btGenerateQRCode;
    private Button btCancel;
    private Button btOk;
    private ProgressBar progressBar;
    private DatabaseReference mDatabase;
    private DatabaseReference userDatabase;
    private FirebaseAuth auth;
    private int PERMISSION_SDCARD = 0;
    private String pathQrCode = "";
    private boolean hasQrCode = false;
    private Email email = new Email();
    private static final String myEmail = "xxxxx";
    private static final String myPassword = "xxxxx";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        iniViews();

        auth = FirebaseAuth.getInstance();
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
                    String email = etEmail.getText().toString();
                    String password = etPassword.getText().toString();
                    String qrCodeContent = "memory-keeper;" + email + ";" + password + ";";
                    BitMatrix bitMatrix = writer.encode(qrCodeContent,
                                                    BarcodeFormat.QR_CODE, 256, 256);
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
                    Log.e("ERROR HERE", "Error: " + e.toString());
                    e.printStackTrace();
                }
            }
        });

        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!checkUserInput()) return;

                isLoading();

                final User user = new User();
                user.setName(etName.getText().toString());
                user.setUsername(etUsername.getText().toString());
                user.setEmail(etEmail.getText().toString());
                user.isQrCode(hasQrCode);

                auth.createUserWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (!task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), getString(R.string.auth_failed) +
                                            task.getException(), Toast.LENGTH_SHORT).show();
                                    System.out.println("Failed: " + task.getException());
                                    stopLoading();
                                } else {
                                    //String key = userDatabase.push().getKey();
                                    FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
                                    String key = fUser.getUid();
                                    user.setUserId(key);
                                    userDatabase.child(key).setValue(user);
                                    if (!TextUtils.isEmpty(pathQrCode))
                                        sendEmail(etEmail.getText().toString());

                                    Toast.makeText(getApplicationContext(),
                                            R.string.email_sent_qrcode, Toast.LENGTH_LONG)
                                            .show();

                                    stopLoading();
                                    finish();
                                }

                            }
                        });


            }
        });
    }

    private void iniViews() {
        etName = (EditText) findViewById(R.id.activitySignUpEtName);
        etUsername = (EditText) findViewById(R.id.activitySignUpEtUsername);
        etEmail = (EditText) findViewById(R.id.activitySignUpEtEmail);
        etPassword = (EditText) findViewById(R.id.activitySignUpEtPassword);
        etRepeatPassword = (EditText) findViewById(R.id.activitySignUpEtRepeatPassword);
        btGenerateQRCode = (Button) findViewById(R.id.activitySignUpBtGenerateQrCode);
        btCancel = (Button) findViewById(R.id.activitySignUpBtCancel);
        btOk = (Button) findViewById(R.id.activitySignUpBtOk);
        progressBar = (ProgressBar) findViewById(R.id.progressBarSignUp);
    }

    private void saveQRCode(Bitmap bmp) {
        Util.isLoading(progressBar, SignUpActivity.this);
        // save imageView in gallery
        String cameraPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString();
        File cachePath = new File(cameraPath + "/memory-keeper-qr-code2.jpg");
        pathQrCode = cachePath.toString();
        try {
            cachePath.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(cachePath);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.close();
            hasQrCode = true;
            stopLoading();
            Toast.makeText(getApplicationContext(), getString(R.string.qrcode_generated) + pathQrCode,
                    Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            stopLoading();
            Log.e("ERROR HERE", "Error: " + e.toString());
            e.printStackTrace();
        }
    }

    private boolean checkUserInput() {
        String name = etName.getText().toString();
        String username = etUsername.getText().toString();
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        String repeatPassword = etRepeatPassword.getText().toString();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(username) || TextUtils.isEmpty(email) ||
                TextUtils.isEmpty(password) || TextUtils.isEmpty(repeatPassword)) {
            Toast.makeText(getApplicationContext(), getString(R.string.fill_all_fields), Toast.LENGTH_SHORT).show();
            return false;
        } else if (!password.equals(repeatPassword)) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.password_does_not_match), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getApplicationContext(), getString(R.string.permission_granted),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), getString(R.string.permission_denied),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void isLoading() {
        Util.isLoading(progressBar, SignUpActivity.this);
    }

    public void stopLoading() {
        Util.stopLoading(progressBar, SignUpActivity.this);
    }

    public void sendEmail(String toUser){
        Properties props = email.getProps();
        Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(myEmail, myPassword);
            }
        });

        session.setDebug(true);

        email.setMyEmail(myEmail);
        email.setToUser(toUser);
        email.setSubject(getString(R.string.email_subject));
        email.setText(getString(R.string.email_body));
        email.setQrCodePath(pathQrCode);

        InternetAsync internetAsync = new InternetAsync(session);
        internetAsync.execute(email);
    }
}
