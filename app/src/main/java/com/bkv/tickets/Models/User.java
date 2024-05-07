package com.bkv.tickets.Models;

import androidx.annotation.NonNull;

import java.util.Date;

public class User {
    private String id;
    private String authid;
    private String email;
    private String name;
    private Date birthDate;

    public User(String id, String authid, String email, String name, Date birthDate) {
        this.id = id;
        this.authid = authid;
        this.email = email;
        this.name = name;
        this.birthDate = birthDate;
    }

    public User() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthid() {
        return authid;
    }

    public void setAuthid(String authid) {
        this.authid = authid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    @NonNull
    @Override
    public String toString() {
        return "User{" +
                "userId='" + id + '\'' +
                ", authid='" + authid + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", birthDate=" + birthDate +
                '}';
    }
}
