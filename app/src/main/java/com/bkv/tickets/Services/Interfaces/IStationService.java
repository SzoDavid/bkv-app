package com.bkv.tickets.Services.Interfaces;

import com.bkv.tickets.Models.Station;
import com.google.android.gms.tasks.OnCompleteListener;

import java.util.List;

public interface IStationService {
    void getAll(OnCompleteListener<List<Station>> onCompleteListener);
}
