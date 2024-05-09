package com.bkv.tickets.Models;

import androidx.annotation.NonNull;

import java.time.LocalDateTime;

public class TimeTableElement {
    private Station station;
    private LocalDateTime departureTime;

    public TimeTableElement(Station station, LocalDateTime departureTime) {
        this.station = station;
        this.departureTime = departureTime;
    }

    public TimeTableElement() {
    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    @NonNull
    @Override
    public String toString() {
        return "TimeTableElement{" +
                "station=" + station +
                ", departureTime=" + departureTime +
                '}';
    }
}
