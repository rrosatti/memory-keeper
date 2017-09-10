package com.example.rrosatti.memorykeeper.model;

import android.graphics.Bitmap;

/**
 * Created by rrosatti on 9/10/17.
 */

public class Memory {

    private long id;
    private String title;
    private String description;
    private Bitmap img;

    public Memory() {}

    public Memory(long id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.img = null;
    }

    public Memory(long id, String title, String description, Bitmap img) {
        this(id, title, description);
        this.img = img;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Bitmap getImg() {
        return img;
    }

    public void setImg(Bitmap img) {
        this.img = img;
    }
}
