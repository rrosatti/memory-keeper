package com.example.rrosatti.memorykeeper.activity;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.rrosatti.memorykeeper.R;
import com.example.rrosatti.memorykeeper.adapter.MemoryAdapter;
import com.example.rrosatti.memorykeeper.model.Memory;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MemoryListActivity extends AppCompatActivity {

    private FloatingActionButton btNewMemory;
    private RecyclerView listOfMemories;
    private DatabaseReference database;
    private DatabaseReference userReference;
    private DatabaseReference memoriesDatabase;
    private String userId;
    private MemoryAdapter memoryAdapter;
    private List<Memory> memories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_list);

        iniViews();

        userId = getIntent().getStringExtra("userId");

        database = FirebaseDatabase.getInstance().getReference();
        userReference = database.child("users").child(userId);

        memories = new ArrayList<>();

        Query getMemories = database.child("memories").orderByChild("userId").equalTo(userId);
        getMemories.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Memory memory = ds.getValue(Memory.class);
                        memories.add(memory);
                        memoryAdapter.add(memory);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Warning", "Event canceled!");
            }
        });

        memoryAdapter = new MemoryAdapter(this, new ArrayList<Memory>(0), new MemoryAdapter.MemoryListener() {
            @Override
            public void onMemoryClick(String memoryId) {
                Toast.makeText(getApplicationContext(), "MemoryID: " + memoryId, Toast.LENGTH_SHORT).show();
            }
        });
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 1);
        listOfMemories.setLayoutManager(layoutManager);
        listOfMemories.setItemAnimator(new DefaultItemAnimator());
        listOfMemories.setAdapter(memoryAdapter);

        btNewMemory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inNewMemory = new Intent(MemoryListActivity.this, NewMemoryActivity.class);
                inNewMemory.putExtra("userId", userId);
                startActivity(inNewMemory);
            }
        });
    }

    private void iniViews() {
        btNewMemory = (FloatingActionButton) findViewById(R.id.btNewMemory);
        listOfMemories = (RecyclerView) findViewById(R.id.listMemories);
    }
}
