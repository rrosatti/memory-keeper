package com.example.rrosatti.memorykeeper.model;

import android.graphics.Bitmap;
import android.hardware.fingerprint.FingerprintManager;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by rrosatti on 9/10/17.
 */

public class User {

    private String userId;
    private String name;
    private String username;
    private String email;
    private boolean qrCode;

    public User() {}

    public User(String userId, String name, String username, String email, boolean qrCode) {
        this.userId = userId;
        this.name = name;
        this.username = username;
        this.email = email;
        this.qrCode = qrCode;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean getQrCode() {
        return qrCode;
    }

    public void isQrCode(boolean qrCode) {
        this.qrCode = qrCode;
    }

    public void setEmail(String email) { this.email = email; }

    public String getEmail() { return email; }

}
