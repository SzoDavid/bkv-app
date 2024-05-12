package com.bkv.tickets.Models;

import androidx.annotation.NonNull;

public class TrainSearchResult {
    private Train train;
    private TimeTableElement start;
    private TimeTableElement destination;

    public TrainSearchResult(Train train, TimeTableElement start, TimeTableElement destination) {
        this.train = train;
        this.start = start;
        this.destination = destination;
    }

    public TrainSearchResult() {
    }

    public Train getTrain() {
        return train;
    }

    public TrainSearchResult setTrain(Train train) {
        this.train = train;
        return this;
    }

    public TimeTableElement getStart() {
        return start;
    }

    public TrainSearchResult setStart(TimeTableElement start) {
        this.start = start;
        return this;
    }

    public TimeTableElement getDestination() {
        return destination;
    }

    public TrainSearchResult setDestination(TimeTableElement destination) {
        this.destination = destination;
        return this;
    }

    @NonNull
    @Override
    public String toString() {
        return "TrainSearchResult{" +
                "train=" + train +
                ", start=" + start +
                ", destination=" + destination +
                '}';
    }
}
