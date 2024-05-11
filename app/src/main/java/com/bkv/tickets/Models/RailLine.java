package com.bkv.tickets.Models;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.DocumentReference;

import java.util.List;

public class RailLine {
    private DocumentReference reference;
    private String id;
    private List<Stop> stations;

    public RailLine(String id, List<Stop> stations) {
        this.id = id;
        this.stations = stations;
    }

    public RailLine(DocumentReference reference) {
        this.reference = reference;
    }

    public RailLine() {
    }

    public DocumentReference getReference() {
        return reference;
    }

    public RailLine setReference(DocumentReference reference) {
        this.reference = reference;
        return this;
    }

    public String getId() {
        return id;
    }

    public RailLine setId(String id) {
        this.id = id;
        return this;
    }

    public List<Stop> getStations() {
        return stations;
    }

    public RailLine setStations(List<Stop> stations) {
        this.stations = stations;
        return this;
    }

    @NonNull
    @Override
    public String toString() {
        return "RailLine{" +
                "railLineId='" + id + '\'' +
                ", stations=" + stations +
                '}';
    }
}
