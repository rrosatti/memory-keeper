package com.example.rrosatti.memorykeeper.model;

import android.graphics.Bitmap;
import android.hardware.fingerprint.FingerprintManager;

/**
 * Created by rrosatti on 9/10/17.
 */

public class User {

    private long id;
    private String name;
    private String username;
    private String password;
    private Bitmap qrCode;
    private FingerprintManager fingerprintManager; // I don't know if it's the right one

    public User() {}

    public User(long id, String name, String username, String password) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
        this.qrCode = null;
        this.fingerprintManager = null;
    }

    public User(long id, String name, String username, String password, Bitmap qrCode) {
        this(id, name, username, password);
        this.qrCode = qrCode;
    }

    public User(long id, String name, String username, String password, FingerprintManager fingerprintManager) {
        this(id, name, username, password);
        this.fingerprintManager = fingerprintManager;
    }

    public User(long id, String name, String username, String password,
                Bitmap qrCode, FingerprintManager fingerprintManager) {
        this(id, name, username, password, qrCode);
        this.fingerprintManager = fingerprintManager;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public Bitmap getQrCode() {
        return qrCode;
    }

    public void setQrCode(Bitmap qrCode) {
        this.qrCode = qrCode;
    }

    public FingerprintManager getFingerprintManager() {
        return fingerprintManager;
    }

    public void setFingerprintManager(FingerprintManager fingerprintManager) {
        this.fingerprintManager = fingerprintManager;
    }
}
