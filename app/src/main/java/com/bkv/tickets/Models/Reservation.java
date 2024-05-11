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

    public Reservation setId(String id) {
        this.id = id;
        return this;
    }

    public Station getFrom() {
        return from;
    }

    public Reservation setFrom(Station from) {
        this.from = from;
        return this;
    }

    public Station getTo() {
        return to;
    }

    public Reservation setTo(Station to) {
        this.to = to;
        return this;
    }

    public Train getTrain() {
        return train;
    }

    public Reservation setTrain(Train train) {
        this.train = train;
        return this;
    }

    public User getUser() {
        return user;
    }

    public Reservation setUser(User user) {
        this.user = user;
        return this;
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
