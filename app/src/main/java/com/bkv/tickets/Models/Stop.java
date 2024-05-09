package com.bkv.tickets.Models;

import androidx.annotation.NonNull;

public class Stop {
    private Station station;
    private int durationMinutes;

    public Stop(Station station, int durationMinutes) {
        this.station = station;
        this.durationMinutes = durationMinutes;
    }

    public Stop() {
    }

    public Station getStation() {
        return station;
    }

    public Stop setStation(Station station) {
        this.station = station;
        return this;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public Stop setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
        return this;
    }

    @NonNull
    @Override
    public String toString() {
        return "Stop{" +
                "station=" + station +
                ", minutes=" + durationMinutes +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Stop stop = (Stop) o;

        return station.equals(stop.station);
    }
}
