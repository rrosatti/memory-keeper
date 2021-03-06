package com.example.rrosatti.memorykeeper.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

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
import java.util.List;

public class MemoryListActivity extends AppCompatActivity {

    private static final String MY_PREFERENCES = "myPreferences";
    private FloatingActionButton btNewMemory;
    private RecyclerView listOfMemories;
    private DatabaseReference database;
    private String userId;
    private List<Memory> memories;
    private FirebaseRecyclerAdapter firebaseAdapter;
    private ProgressBar progressBar;
    private FloatingActionButton btLogout;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_list);

        iniViews();

        sharedPreferences = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        progressBar.setVisibility(View.VISIBLE);
        userId = getIntent().getStringExtra("userId");

        database = FirebaseDatabase.getInstance().getReference();

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
                } else{
                    //Toast.makeText(getApplicationContext(),"Deu ruim",Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Warning", "Event canceled!");
            }
        });

        if (savedInstanceState != null) {
            listOfMemories.scrollToPosition(savedInstanceState.getInt("lastPosition"));
        }

        btNewMemory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inNewMemory = new Intent(MemoryListActivity.this, NewMemoryActivity.class);
                inNewMemory.putExtra("userId", userId);
                startActivityForResult(inNewMemory, 1);
            }
        });

        btLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLogoutDialog();
            }
        });
    }

    private void iniViews() {
        btNewMemory = (FloatingActionButton) findViewById(R.id.btNewMemory);
        listOfMemories = (RecyclerView) findViewById(R.id.listMemories);
        progressBar = (ProgressBar) findViewById(R.id.progressBarMemoryList);
        btLogout = (FloatingActionButton) findViewById(R.id.btLogout);
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putInt("lastPosition", listOfMemories.getVerticalScrollbarPosition());
    }

    public void showLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(R.string.confirm).setMessage(R.string.logout);

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("remember", false);
                editor.apply();
                finish();
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        builder.show();
    }
}
