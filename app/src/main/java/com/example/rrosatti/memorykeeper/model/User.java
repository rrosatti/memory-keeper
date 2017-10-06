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
    private String password;
    private String qrCode; //Bitmap
    private String fingerprint; // FingerprintManager
    private String memoryId;

    public User() {}

    public User(String userId, String name, String username, String password, String qrCode,
                String fingerprint, String memoryId) {
        this.userId = userId;
        this.name = name;
        this.username = username;
        this.password = password;
        this.qrCode = qrCode;
        this.fingerprint = fingerprint;
        this.memoryId = memoryId;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }

    public String getMemoryId() {
        return memoryId;
    }

    public void setMemoryId(String memoryId) {
        this.memoryId = memoryId;
    }


    //TODO: create methods to convert String to Bitmap and FingerprintManager?
}
