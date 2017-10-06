package com.example.rrosatti.memorykeeper.model;

import android.graphics.Bitmap;

/**
 * Created by rrosatti on 9/10/17.
 */

public class Memory {

    private String memoryId;
    private String title;
    private String description;
    private String img; // Bitmap

    public Memory() {}

    public Memory(String memoryId, String title, String description) {
        this.memoryId = memoryId;
        this.title = title;
        this.description = description;
        this.img = null;
    }

    public Memory(String memoryId, String title, String description, String img) {
        this(memoryId, title, description);
        this.img = img;
    }

    public String getId() {
        return memoryId;
    }

    public void setId(String id) {
        this.memoryId = id;
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

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }


    //TODO: create a method to convert from String to Bitmap
}
