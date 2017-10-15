package com.example.rrosatti.memorykeeper.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rrosatti.memorykeeper.R;
import com.example.rrosatti.memorykeeper.model.Memory;

import java.util.List;

/**
 * Created by rrosatti on 10/14/17.
 */

public class MemoryAdapter extends RecyclerView.Adapter<MemoryAdapter.ViewHolder> {

    private Context context;
    private List<Memory> memories;
    private MemoryListener memoryListener;

    public MemoryAdapter(Context context, List<Memory> memories, MemoryListener memoryListener) {
        this.context = context;
        this.memories = memories;
        this.memoryListener = memoryListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView imgMemory;
        public TextView txtTitle;
        public TextView txtDescription;
        MemoryListener memoryListener;

        public ViewHolder(View view, MemoryListener memoryListener) {
            super(view);
            imgMemory = view.findViewById(R.id.imgMemory);
            txtTitle = view.findViewById(R.id.txtTitle);
            txtDescription = view.findViewById(R.id.txtDescription);

            this.memoryListener = memoryListener;
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            // TODO: implement here
            Memory memory = getMemory(getAdapterPosition());
            this.memoryListener.onMemoryClick(memory.getMemoryId());
            notifyDataSetChanged();
        }
    }

    @Override
    public MemoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View memoryView = inflater.inflate(R.layout.memory_item, parent, false);
        ViewHolder holder = new ViewHolder(memoryView, this.memoryListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(MemoryAdapter.ViewHolder holder, int position) {
        Memory memory = memories.get(position);
        ImageView imgMemory = holder.imgMemory;
        TextView txtTitle = holder.txtTitle;
        TextView txtDescription = holder.txtDescription;

        // imgMemory() not working yet
        txtTitle.setText(memory.getTitle());
        txtDescription.setText(memory.getDescription());
    }

    @Override
    public int getItemCount() {
        return (memories == null ? 0 : memories.size());
    }

    public void updateMemories(List<Memory> memories) {
        this.memories = memories;
        notifyDataSetChanged();
    }

    public Memory getMemory(int adapterPosition) {
        return memories.get(adapterPosition);
    }

    public interface MemoryListener {
        void onMemoryClick(String memoryId);
    }

    public void add(Memory memory) {
        memories.add(memory);
        notifyItemInserted(memories.size() - 1);
    }

    public void addAll(List<Memory> memories) {
        for (Memory memory : memories) {
            add(memory);
        }
    }

    public List<Memory> getMemories() {
        return memories;
    }
}
