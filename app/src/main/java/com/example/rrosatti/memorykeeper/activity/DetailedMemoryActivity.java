package com.example.rrosatti.memorykeeper.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rrosatti.memorykeeper.R;
import com.example.rrosatti.memorykeeper.model.Memory;
import com.example.rrosatti.memorykeeper.utils.Util;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.TimeZone;

public class DetailedMemoryActivity extends AppCompatActivity {

    private EditText etTitle;
    private ImageView imgMemory;
    private EditText etDescription;
    private Button btUpdate;
    private ProgressBar progressBar;
    private TextView txtDate;
    private String memoryId;
    private DatabaseReference database;
    private DatabaseReference memoriesDatabase;
    private Memory memory;
    private String sDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_memory);

        iniViews();
        memoryId = getIntent().getStringExtra("memoryId");
        if (memoryId == null) {
            Toast.makeText(this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
            finish();
        }

        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressBar.setVisibility(View.VISIBLE);
        database = FirebaseDatabase.getInstance().getReference();
        memoriesDatabase = database.child("memories");

        memoriesDatabase.child(memoryId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.GONE);
                memory = dataSnapshot.getValue(Memory.class);
                etTitle.setText(memory.getTitle());
                etDescription.setText(memory.getDescription());
                txtDate.setText(memory.getDate());
                if (!memory.getImg().isEmpty())
                    Picasso.with(getApplicationContext()).load(memory.getImg()).into(imgMemory);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Warning", "Firebase on DetailedMemoryActivity was just cancelled.");
                progressBar.setVisibility(View.GONE);
            }
        });

        btUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertDialog();
            }
        });

        txtDate.setOnClickListener(new View.OnClickListener() {
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
                DatePickerDialog dialog = new DatePickerDialog(DetailedMemoryActivity.this,
                        R.style.AppTheme_NoActionBar, dateSetListener, cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));

                dialog.show();
            }
        });

    }

    private void iniViews() {
        etTitle = (EditText) findViewById(R.id.activityDetailedMemoryEtTitle);
        imgMemory = (ImageView) findViewById(R.id.activityDetailedMemoryImgMemory);
        etDescription = (EditText) findViewById(R.id.activityDetailedMemoryEtDescription);
        btUpdate = (Button) findViewById(R.id.activityDetailedMemoryBtUpdate);
        progressBar = (ProgressBar) findViewById(R.id.progressBarDetailedMemory);
        txtDate = (TextView) findViewById(R.id.activityDetailedMemoryTxtDate);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }

    public void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(DetailedMemoryActivity.this);

        builder.setTitle(getResources().getString(R.string.confirm))
                .setMessage(getResources().getString(R.string.are_you_sure));

        builder.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (!checkFields()) {
                    return;
                }

                memory.setTitle(etTitle.getText().toString());
                memory.setDate(txtDate.getText().toString());
                memory.setDescription(etDescription.getText().toString());
                database.child("memories").child(memoryId).updateChildren(memory.toMap());
                finish();
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.show();
    }

    private boolean checkFields() {
        if (Util.isViewEmpty(etTitle) || Util.isViewEmpty(txtDate) || Util.isViewEmpty(etDescription)) {
            Toast.makeText(getApplicationContext(), getString(R.string.fields_should_not_be_empty),
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
