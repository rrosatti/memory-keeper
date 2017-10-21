package com.example.rrosatti.memorykeeper.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rrosatti.memorykeeper.R;
import com.example.rrosatti.memorykeeper.activity.DetailedMemoryActivity;
import com.example.rrosatti.memorykeeper.model.Memory;

/**
 * Created by rrosatti on 10/15/17.
 */

public class FirebaseMemoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    private View view;
    private Context context;
    private Memory memory;

    public FirebaseMemoryViewHolder(View view) {
        super(view);
        this.view = view;
        this.context = view.getContext();
        this.view.setOnClickListener(this);
    }

    public void bindMemory(Memory memory) {
        this.memory = memory;
        ImageView imgMemory = view.findViewById(R.id.imgMemory);
        TextView txtTitle = view.findViewById(R.id.txtTitle);
        TextView txtDescription = view.findViewById(R.id.txtDescription);
        TextView txtDate = view.findViewById(R.id.txtDate);

        // imgMemory() not working yet
        txtTitle.setText(memory.getTitle());
        txtDescription.setText(memory.getDescription());
        txtDate.setText(memory.getDate());
    }

    @Override
    public void onClick(View view) {
        Intent inDetailedMemory = new Intent(context.getApplicationContext(), DetailedMemoryActivity.class);
        inDetailedMemory.putExtra("memoryId", memory.getMemoryId());
        context.startActivity(inDetailedMemory);
    }
}
