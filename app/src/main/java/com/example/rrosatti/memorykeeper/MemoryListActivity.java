package com.example.rrosatti.memorykeeper;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MemoryListActivity extends AppCompatActivity {

    private FloatingActionButton btNewMemory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_list);

        iniViews();

        btNewMemory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inNewMemory = new Intent(MemoryListActivity.this, NewMemoryActivity.class);
                startActivity(inNewMemory);
            }
        });
    }

    private void iniViews() {
        btNewMemory = (FloatingActionButton) findViewById(R.id.bt_new_memory);
    }
}
