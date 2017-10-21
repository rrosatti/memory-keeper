package com.example.rrosatti.memorykeeper.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.rrosatti.memorykeeper.R;
import com.example.rrosatti.memorykeeper.model.Memory;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DetailedMemoryActivity extends AppCompatActivity {

    private EditText etTitle;
    private ImageView imgMemory;
    private EditText etDescription;
    private Button btUpdate;
    private ProgressBar progressBar;
    private EditText etDate;
    private String memoryId;
    private DatabaseReference database;
    private DatabaseReference memoriesDatabase;
    private Memory memory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_memory);

        iniViews();
        memoryId = getIntent().getStringExtra("memoryId");
        if (memoryId == null) {
            Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT).show();
            finish();
        }
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
                etDate.setText(memory.getDate());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Warning", "Firebase on DetailedMemoryActivity was just cancelled.");
                progressBar.setVisibility(View.GONE);
            }
        });

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void iniViews() {
        etTitle = (EditText) findViewById(R.id.activityDetailedMemoryEtTitle);
        imgMemory = (ImageView) findViewById(R.id.activityDetailedMemoryImgMemory);
        etDescription = (EditText) findViewById(R.id.activityDetailedMemoryEtDescription);
        btUpdate = (Button) findViewById(R.id.activityDetailedMemoryBtUpdate);
        progressBar = (ProgressBar) findViewById(R.id.progressBarDetailedMemory);
        etDate = (EditText) findViewById(R.id.activityDetailedMemoryEtDate);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }
}
