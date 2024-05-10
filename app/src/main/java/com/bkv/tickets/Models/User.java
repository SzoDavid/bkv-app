package com.bkv.tickets.Models;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.Exclude;

public class User {
    @Exclude
    private String id;
    private String authId;
    private String email;
    private String name;

    public User(String id, String authId, String email, String name) {
        this.id = id;
        this.authId = authId;
        this.email = email;
        this.name = name;
    }

    public User() {
    }

    @Exclude
    public String getId() {
        return this.id;
    }

    @Exclude
    public User setId(String id) {
        this.id = id;
        return this;
    }

    public String getAuthId() {
        return authId;
    }

    public User setAuthId(String authId) {
        this.authId = authId;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getName() {
        return name;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    @NonNull
    @Override
    public String toString() {
        return "User{" +
                "userId='" + id + '\'' +
                ", authid='" + authId + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
