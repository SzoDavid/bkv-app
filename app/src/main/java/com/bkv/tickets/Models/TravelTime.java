package com.bkv.tickets.Models;

import androidx.annotation.NonNull;

public class TravelTime {
    private String fromStationId;
    private String toStationId;
    private int durationMinutes;

    public TravelTime(String from, String to, int durationMinutes) {
        this.fromStationId = from;
        this.toStationId = to;
        this.durationMinutes = durationMinutes;
    }



    public String getFrom() {
        return fromStationId;
    }

    public void setFrom(String from) {
        this.fromStationId = from;
    }

    public String getTo() {
        return toStationId;
    }

    public void setTo(String to) {
        this.toStationId = to;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    @NonNull
    @Override
    public String toString() {
        return "TravelTime{" +
                "from=" + fromStationId +
                ", to=" + toStationId +
                ", durationMinutes=" + durationMinutes +
                '}';
    }
}
