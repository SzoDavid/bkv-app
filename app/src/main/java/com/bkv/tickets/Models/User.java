package com.bkv.tickets.Models;

import androidx.annotation.NonNull;

public class User {
    private String id;
    private String authid;
    private String email;
    private String name;

    public User(String id, String authid, String email, String name) {
        this.id = id;
        this.authid = authid;
        this.email = email;
        this.name = name;
    }

    public User() {
    }

    public String getId() {
        return id;
    }

    public User setId(String id) {
        this.id = id;
        return this;
    }

    public String getAuthid() {
        return authid;
    }

    public User setAuthid(String authid) {
        this.authid = authid;
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
                ", authid='" + authid + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
