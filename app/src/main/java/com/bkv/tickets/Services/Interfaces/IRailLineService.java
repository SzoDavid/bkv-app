package com.bkv.tickets.Services.Interfaces;

import com.bkv.tickets.Models.RailLine;
import com.bkv.tickets.Models.Station;
import com.google.android.gms.tasks.OnCompleteListener;

public interface IRailLineService {
    void getById(String railLineId, OnCompleteListener<RailLine> onCompleteListener);
    void getAllByStartAndDestinationStations(Station start, Station destination, OnCompleteListener<Void> onCompleteListener);
}
