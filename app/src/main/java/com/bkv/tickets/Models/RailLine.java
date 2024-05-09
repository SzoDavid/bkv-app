package com.bkv.tickets.Models;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.Map;

public class RailLine {
    private String id;
    private List<Stop> stations;

    public RailLine(String id, List<Stop> stations) {
        this.id = id;
        this.stations = stations;
    }

    public RailLine() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Stop> getStations() {
        return stations;
    }

    public void setStations(List<Stop> stations) {
        this.stations = stations;
    }

    @NonNull
    @Override
    public String toString() {
        return "RailLine{" +
                "railLineId='" + id + '\'' +
                ", stations=" + stations +
                '}';
    }
}
