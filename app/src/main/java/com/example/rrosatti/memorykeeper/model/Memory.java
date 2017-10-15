package com.example.rrosatti.memorykeeper.model;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by rrosatti on 9/10/17.
 */

public class Memory implements Serializable {

    private String memoryId;
    private String title;
    private String description;
    private String img; // Bitmap
    private String userId;

    public Memory() {}

    public Memory(String memoryId, String title, String description, String userId) {
        this.memoryId = memoryId;
        this.title = title;
        this.description = description;
        this.img = null;
        this.userId = userId;
    }

    public Memory(String memoryId, String title, String description, String img, String userId) {
        this(memoryId, title, description, userId);
        this.img = img;
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

    public String getMemoryId() {
        return memoryId;
    }

    public void setMemoryId(String memoryId) {
        this.memoryId = memoryId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    //TODO: create a method to convert from String to Bitmap
}
