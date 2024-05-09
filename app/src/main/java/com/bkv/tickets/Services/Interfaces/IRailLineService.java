package com.bkv.tickets.Services.Interfaces;

import com.bkv.tickets.Models.RailLine;
import com.bkv.tickets.Models.Station;
import com.google.android.gms.tasks.OnCompleteListener;

import java.time.LocalTime;
import java.util.Map;

public interface IRailLineService {
    void getById(String railLineId, OnCompleteListener<Void> onCompleteListener);
    void getAll(OnCompleteListener<Void> onCompleteListener);
    void getAllByStartAndDestinationStations(Station start, Station destination, OnCompleteListener<Void> onCompleteListener);
}
