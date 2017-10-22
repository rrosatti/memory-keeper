package com.example.rrosatti.memorykeeper.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rrosatti.memorykeeper.R;
import com.example.rrosatti.memorykeeper.model.Memory;
import com.example.rrosatti.memorykeeper.utils.Util;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.Calendar;
import java.util.TimeZone;

public class NewMemoryActivity extends AppCompatActivity {

    private EditText etTitle;
    private EditText etLongDescription;
    private Button btAddImage;
    private Button btCancel;
    private Button btOk;
    private ImageButton btCalendar;
    private TextView txtDate;
    private TextView txtImageName;
    private ProgressBar progressBar;
    private DatabaseReference database;
    private DatabaseReference memoriesDatabase;
    private String userId;
    private Memory memory;
    private String sDate = "";
    private static final int SELECT_PICTURE = 1;
    private String selectedImagePath = "";
    private String downloadFirebaseImageUrl = "";
    private boolean imageSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_memory);

        iniViews();

        userId = getIntent().getStringExtra("userId");

        if (userId == null) {
            Toast.makeText(this, "Something went really wrong!", Toast.LENGTH_SHORT).show();
            finish();
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        sDate = Util.getCurrentDate();
        txtDate.setText(sDate);

        database = FirebaseDatabase.getInstance().getReference();
        memoriesDatabase = database.child("memories");

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });

        btCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        sDate = Util.toStringDate(day, month+1, year);
                        txtDate.setText(sDate);
                    }
                };

                Calendar cal = Calendar.getInstance(TimeZone.getDefault());
                DatePickerDialog dialog = new DatePickerDialog(NewMemoryActivity.this, R.style.AppTheme_NoActionBar,
                        dateSetListener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH));

                dialog.show();
            }
        });

        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkFields())
                    return;

                progressBar.setVisibility(View.VISIBLE);
                if (imageSelected)
                    saveImageFirebase();
                else
                    saveMemoryFirebase();
            }
        });

        btAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(in, SELECT_PICTURE);
            }
        });

    }

    private void iniViews() {
        etTitle = (EditText) findViewById(R.id.activityNewMemoryEtTitle);
        etLongDescription = (EditText) findViewById(R.id.activityNewMemoryEtDescription);
        btAddImage = (Button) findViewById(R.id.activityNewMemoryBtAddImage);
        btCancel = (Button) findViewById(R.id.activityNewMemoryBtCancel);
        btOk = (Button) findViewById(R.id.activityNewMemoryBtOk);
        btCalendar = (ImageButton) findViewById(R.id.btCalendar);
        txtDate = (TextView) findViewById(R.id.txtShowDate);
        txtImageName = (TextView) findViewById(R.id.txtImageName);
        progressBar = (ProgressBar) findViewById(R.id.progressBarNewMemory);
    }

    @Override
    public void finish() {
        Intent returnNewMemory = new Intent();
        returnNewMemory.putExtra("newMemory", memory);
        setResult(Activity.RESULT_OK, returnNewMemory);
        super.finish();
    }

    private boolean checkFields() {
        if (TextUtils.isEmpty(etTitle.getText().toString())
                || TextUtils.isEmpty(etLongDescription.getText().toString())) {
            Toast.makeText(getApplicationContext(), "You must fill all fields!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        setResult(Activity.RESULT_CANCELED);
        onBackPressed();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                selectedImagePath = getPath(selectedImageUri);
                imageSelected = true;
                txtImageName.setText(selectedImagePath);
                txtImageName.setVisibility(View.VISIBLE);
            }
        }
    }

    public void saveImageFirebase() {
        Uri file = Uri.fromFile(new File(selectedImagePath));
        StorageReference storageRef = FirebaseStorage.getInstance().getReference()
                .child(userId+"/images/"+file.getLastPathSegment());

        UploadTask uploadTask = storageRef.putFile(file);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Error: " + e.toString(), Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                @SuppressWarnings("VisibleForTests") Uri downloadUri = taskSnapshot.getDownloadUrl();
                downloadFirebaseImageUrl = downloadUri.toString();
                saveMemoryFirebase();
            }
        });
    }

    public void saveMemoryFirebase() {
        memory = new Memory();
        memory.setTitle(etTitle.getText().toString());
        memory.setDescription(etLongDescription.getText().toString());
        memory.setImg(downloadFirebaseImageUrl);
        memory.setUserId(userId);
        memory.setDate(sDate);

        String memoryId = memoriesDatabase.push().getKey();
        memory.setMemoryId(memoryId);

        memoriesDatabase.child(memoryId).setValue(memory);

        progressBar.setVisibility(View.GONE);
        finish();
    }

    public String getPath(Uri uri) {
        if (uri == null) {
            return null;
        }

        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(
                uri, projection, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(projection[0]);
        return cursor.getString(columnIndex);
    }
}
