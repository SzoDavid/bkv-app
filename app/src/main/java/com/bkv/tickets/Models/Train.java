package com.bkv.tickets.Models;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.DocumentReference;

import java.time.LocalDateTime;

public class Train {
    private DocumentReference reference;
    private String id;
    private String name;
    private RailLine line;
    private boolean ascendingOrder;
    private LocalDateTime departure;

    public Train(String id, String name, RailLine line, boolean ascendingOrder, LocalDateTime departure) {
        this.id = id;
        this.name = name;
        this.line = line;
        this.ascendingOrder = ascendingOrder;
        this.departure = departure;
    }

    public Train(DocumentReference reference) {
        this.reference = reference;
    }

    public Train() {
    }

    public DocumentReference getReference() {
        return reference;
    }

    public Train setReference(DocumentReference reference) {
        this.reference = reference;
        return this;
    }

    public String getId() {
        return id;
    }

    public Train setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Train setName(String name) {
        this.name = name;
        return this;
    }

    public RailLine getLine() {
        return line;
    }

    public Train setLine(RailLine line) {
        this.line = line;
        return this;
    }

    public boolean isAscendingOrder() {
        return ascendingOrder;
    }

    public Train setAscendingOrder(boolean ascendingOrder) {
        this.ascendingOrder = ascendingOrder;
        return this;
    }

    public LocalDateTime getDeparture() {
        return departure;
    }

    public Train setDeparture(LocalDateTime departure) {
        this.departure = departure;
        return this;
    }

    @NonNull
    @Override
    public String toString() {
        return "Train{" +
                "trainId='" + id + '\'' +
                ", name='" + name + '\'' +
                ", line=" + line +
                ", ascendingOrder=" + ascendingOrder +
                ", departure=" + departure +
                '}';
    }
}
