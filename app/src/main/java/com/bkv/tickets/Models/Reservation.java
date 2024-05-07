package com.bkv.tickets.Models;

import androidx.annotation.NonNull;

public class Reservation {
    private String id;
    private Station from;
    private Station to;
    private Train train;
    private User user;

    public Reservation(String id, Station from, Station to, Train train, User user) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.train = train;
        this.user = user;
    }

    public Reservation() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Station getFrom() {
        return from;
    }

    public void setFrom(Station from) {
        this.from = from;
    }

    public Station getTo() {
        return to;
    }

    public void setTo(Station to) {
        this.to = to;
    }

    public Train getTrain() {
        return train;
    }

    public void setTrain(Train train) {
        this.train = train;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @NonNull
    @Override
    public String toString() {
        return "Reservation{" +
                "id='" + id + '\'' +
                ", from=" + from +
                ", to=" + to +
                ", train=" + train +
                ", user=" + user +
                '}';
    }
}
