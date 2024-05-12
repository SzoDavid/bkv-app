package com.bkv.tickets.Models;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

import java.util.Map;

public class ReservationSaveObject {
    private Timestamp departTime;
    Map<String, Object> from;
    Map<String, Object> to;
    private DocumentReference train;
    private String trainName;
    private DocumentReference user;

    public ReservationSaveObject(Timestamp departTime, Map<String, Object> from, Map<String, Object> to, DocumentReference train, String trainName, DocumentReference user) {
        this.departTime = departTime;
        this.from = from;
        this.to = to;
        this.train = train;
        this.trainName = trainName;
        this.user = user;
    }

    public ReservationSaveObject() {
    }

    public Timestamp getDepartTime() {
        return departTime;
    }

    public ReservationSaveObject setDepartTime(Timestamp departTime) {
        this.departTime = departTime;
        return this;
    }

    public Map<String, Object> getFrom() {
        return from;
    }

    public ReservationSaveObject setFrom(Map<String, Object> from) {
        this.from = from;
        return this;
    }

    public Map<String, Object> getTo() {
        return to;
    }

    public ReservationSaveObject setTo(Map<String, Object> to) {
        this.to = to;
        return this;
    }

    public DocumentReference getTrain() {
        return train;
    }

    public ReservationSaveObject setTrain(DocumentReference train) {
        this.train = train;
        return this;
    }

    public String getTrainName() {
        return trainName;
    }

    public ReservationSaveObject setTrainName(String trainName) {
        this.trainName = trainName;
        return this;
    }

    public DocumentReference getUser() {
        return user;
    }

    public ReservationSaveObject setUser(DocumentReference user) {
        this.user = user;
        return this;
    }
}
