package com.bkv.tickets.Models;

import androidx.annotation.NonNull;

import java.time.LocalDateTime;

public class Train {
    private String id;
    private String Name;
    private int totalSeats;
    private int availableSeats;
    private RailLine line;
    private boolean ascendingOrder;
    private LocalDateTime departure;

    public Train(String id, String name, int totalSeats, int availableSeats, RailLine line, boolean ascendingOrder, LocalDateTime departure) {
        this.id = id;
        Name = name;
        this.totalSeats = totalSeats;
        this.availableSeats = availableSeats;
        this.line = line;
        this.ascendingOrder = ascendingOrder;
        this.departure = departure;
    }

    public Train() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(int totalSeats) {
        this.totalSeats = totalSeats;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    public RailLine getLine() {
        return line;
    }

    public void setLine(RailLine line) {
        this.line = line;
    }

    public boolean isAscendingOrder() {
        return ascendingOrder;
    }

    public void setAscendingOrder(boolean ascendingOrder) {
        this.ascendingOrder = ascendingOrder;
    }

    public LocalDateTime getDeparture() {
        return departure;
    }

    public void setDeparture(LocalDateTime departure) {
        this.departure = departure;
    }

    @NonNull
    @Override
    public String toString() {
        return "Train{" +
                "trainId='" + id + '\'' +
                ", Name='" + Name + '\'' +
                ", totalSeats=" + totalSeats +
                ", availableSeats=" + availableSeats +
                ", line=" + line.getId() +
                ", ascendingOrder=" + ascendingOrder +
                ", departure=" + departure +
                '}';
    }
}
