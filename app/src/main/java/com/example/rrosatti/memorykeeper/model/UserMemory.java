package com.example.rrosatti.memorykeeper.model;

/**
 * Created by rrosatti on 9/10/17.
 */

public class UserMemory {

    private long id;
    private long userId;
    private long memoryId;

    public UserMemory() {}

    public UserMemory(long id, long userId, long memoryId) {
        this.id = id;
        this.userId = userId;
        this.memoryId = memoryId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getMemoryId() {
        return memoryId;
    }

    public void setMemoryId(long memoryId) {
        this.memoryId = memoryId;
    }
}
