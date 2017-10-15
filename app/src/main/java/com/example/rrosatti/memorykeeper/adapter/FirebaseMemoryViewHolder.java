package com.example.rrosatti.memorykeeper.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rrosatti.memorykeeper.R;
import com.example.rrosatti.memorykeeper.model.Memory;

/**
 * Created by rrosatti on 10/15/17.
 */

public class FirebaseMemoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    private View view;
    private Context context;

    public FirebaseMemoryViewHolder(View view) {
        super(view);
        this.view = view;
        this.context = view.getContext();
        this.view.setOnClickListener(this);
    }

    public void bindMemory(Memory memory) {
        ImageView imgMemory = view.findViewById(R.id.imgMemory);
        TextView txtTitle = view.findViewById(R.id.txtTitle);
        TextView txtDescription = view.findViewById(R.id.txtDescription);

        // imgMemory() not working yet
        txtTitle.setText(memory.getTitle());
        txtDescription.setText(memory.getDescription());
    }

    @Override
    public void onClick(View view) {

    }
}
