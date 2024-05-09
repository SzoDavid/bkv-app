package com.bkv.tickets.Services.Interfaces;

import com.google.android.gms.tasks.OnCompleteListener;

public interface IStationService {
    void getById(String stationId, OnCompleteListener<Void> onCompleteListener);
    void getAll(OnCompleteListener<Void> onCompleteListener);
    void getAllByName(String name, OnCompleteListener<Void> onCompleteListener);
}
