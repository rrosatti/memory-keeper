package com.example.rrosatti.memorykeeper.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rrosatti.memorykeeper.R;
import com.example.rrosatti.memorykeeper.model.Memory;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;

public class NewMemoryActivity extends AppCompatActivity {

    private EditText etTitle;
    private EditText etLongDescription;
    private Button btAddImage;
    private Button btCancel;
    private Button btOk;
    private DatabaseReference database;
    private DatabaseReference memoriesDatabase;
    private String userId;
    private Memory memory;

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

        database = FirebaseDatabase.getInstance().getReference();
        memoriesDatabase = database.child("memories");

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });

        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkFields())
                    return;

                memory = new Memory();
                memory.setTitle(etTitle.getText().toString());
                memory.setDescription(etLongDescription.getText().toString());
                memory.setImg("/not/working/yet");
                memory.setUserId(userId);

                String memoryId = memoriesDatabase.push().getKey();
                memory.setMemoryId(memoryId);

                memoriesDatabase.child(memoryId).setValue(memory);

                // pass the new memory back to the previous activity;
                finish();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void iniViews() {
        etTitle = (EditText) findViewById(R.id.activityNewMemoryEtTitle);
        etLongDescription = (EditText) findViewById(R.id.activityNewMemoryEtDescription);
        btAddImage = (Button) findViewById(R.id.activityNewMemoryBtAddImage);
        btCancel = (Button) findViewById(R.id.activityNewMemoryBtCancel);
        btOk = (Button) findViewById(R.id.activityNewMemoryBtOk);
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
}
