package com.example.rrosatti.memorykeeper.activity;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.rrosatti.memorykeeper.R;
import com.example.rrosatti.memorykeeper.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MemoryListActivity extends AppCompatActivity {

    private FloatingActionButton btNewMemory;
    private RecyclerView listOfMemories;
    private FirebaseDatabase mDatabase;
    private DatabaseReference userReference;
    private FirebaseDatabase memoriesDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_list);

        iniViews();

        final String userId = getIntent().getStringExtra("userId");

        mDatabase = FirebaseDatabase.getInstance();
        userReference = mDatabase.getReference().child("users").child(userId);

        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                Toast.makeText(getApplicationContext(), "Welcome " + user.getName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        userReference.addValueEventListener(userListener);

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
