package com.example.rrosatti.memorykeeper.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.rrosatti.memorykeeper.R;
import com.example.rrosatti.memorykeeper.adapter.FirebaseMemoryViewHolder;
import com.example.rrosatti.memorykeeper.model.Memory;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemoryListActivity extends AppCompatActivity {

    private FloatingActionButton btNewMemory;
    private RecyclerView listOfMemories;
    private DatabaseReference database;
    private DatabaseReference userReference;
    private DatabaseReference memoriesDatabase;
    private String userId;
    private List<Memory> memories;
    private FirebaseRecyclerAdapter firebaseAdapter;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_list);

        iniViews();

        progressBar.setVisibility(View.VISIBLE);
        userId = getIntent().getStringExtra("userId");

        database = FirebaseDatabase.getInstance().getReference();
        userReference = database.child("users").child(userId);

        memories = new ArrayList<>();

        Query getMemories = database.child("memories").orderByChild("userId").equalTo(userId);
        firebaseAdapter = new FirebaseRecyclerAdapter<Memory, FirebaseMemoryViewHolder>
                (Memory.class, R.layout.memory_item, FirebaseMemoryViewHolder.class, getMemories) {

            @Override
            protected void populateViewHolder(FirebaseMemoryViewHolder viewHolder, Memory model, int position) {
                viewHolder.bindMemory(model);
            }
        };

        listOfMemories.setHasFixedSize(true);
        listOfMemories.setLayoutManager(new GridLayoutManager(this, 1));
        listOfMemories.setAdapter(firebaseAdapter);

        getMemories.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    progressBar.setVisibility(View.GONE);
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Memory memory = ds.getValue(Memory.class);
                        memories.add(memory);
                        Log.d("Interesting", memory.getDescription());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Warning", "Event canceled!");
            }
        });

        btNewMemory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inNewMemory = new Intent(MemoryListActivity.this, NewMemoryActivity.class);
                inNewMemory.putExtra("userId", userId);
                startActivityForResult(inNewMemory, 1);
            }
        });
    }

    private void iniViews() {
        btNewMemory = (FloatingActionButton) findViewById(R.id.btNewMemory);
        listOfMemories = (RecyclerView) findViewById(R.id.listMemories);
        progressBar = (ProgressBar) findViewById(R.id.progressBarMemoryList);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            switch (resultCode) {
                case Activity.RESULT_OK: {
                    Memory newMemory = (Memory) data.getExtras().getSerializable("newMemory");
                    memories.add(newMemory);
                    listOfMemories.scrollToPosition(listOfMemories.getAdapter().getItemCount() - 1);
                    break;
                }
            }
        }
    }
}
