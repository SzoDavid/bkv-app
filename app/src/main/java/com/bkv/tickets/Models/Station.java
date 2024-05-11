package com.bkv.tickets.Models;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.Exclude;

public class Station {
    @Exclude
    private String id;
    private String name;

    public Station(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Station() {
    }

    @Exclude
    public String getId() {
        return id;
    }

    @Exclude
    public Station setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Station setName(String name) {
        this.name = name;
        return this;
    }

    @NonNull
    @Override
    public String toString() {
        return "Station{" +
                "stationId='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Station station = (Station) o;

        return id.equals(station.id);
    }
}
