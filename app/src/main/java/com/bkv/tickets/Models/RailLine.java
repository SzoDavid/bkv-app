package com.bkv.tickets.Models;

import androidx.annotation.NonNull;

import java.util.List;

public class RailLine {
    private String id;
    private List<Station> stations;
    private List<TravelTime> travelTimes;

    public RailLine(String id, List<Station> stations, List<TravelTime> travelTimes) {
        this.id = id;
        this.stations = stations;
        this.travelTimes = travelTimes;
    }

    public RailLine() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Station> getStations() {
        return stations;
    }

    public void setStations(List<Station> stations) {
        this.stations = stations;
    }

    public List<TravelTime> getTravelTimes() {
        return travelTimes;
    }

    public void setTravelTimes(List<TravelTime> travelTimes) {
        this.travelTimes = travelTimes;
    }

    @NonNull
    @Override
    public String toString() {
        return "RailLine{" +
                "railLineId='" + id + '\'' +
                ", stations=" + stations +
                ", travelTimes=" + travelTimes +
                '}';
    }
}
