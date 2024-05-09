package com.bkv.tickets.Services.FirestoreServices;

import androidx.annotation.NonNull;

import com.bkv.tickets.Models.RailLine;
import com.bkv.tickets.Models.Station;
import com.bkv.tickets.Services.Interfaces.IRailLineService;
import com.google.android.gms.tasks.OnCompleteListener;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirestoreRailLineService implements IRailLineService {
    @Override
    public void getById(@NonNull String railLineId, @NonNull OnCompleteListener<Void> onCompleteListener) {

    }

    @Override
    public void getAll(@NonNull OnCompleteListener<Void> onCompleteListener) {

    }

    @Override
    public void getAllByStartAndDestinationStations(@NonNull Station start, @NonNull Station destination, @NonNull OnCompleteListener<Void> onCompleteListener) {

    }
}
