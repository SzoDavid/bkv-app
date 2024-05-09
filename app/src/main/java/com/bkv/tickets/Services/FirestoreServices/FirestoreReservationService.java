package com.bkv.tickets.Services.FirestoreServices;

import com.bkv.tickets.Models.Reservation;
import com.bkv.tickets.Models.User;
import com.bkv.tickets.Services.Interfaces.IReservationService;
import com.google.android.gms.tasks.OnCompleteListener;

public class FirestoreReservationService implements IReservationService {
    @Override
    public void create(Reservation reservation, OnCompleteListener<Void> onCompleteListener) {

    }

    @Override
    public void delete(String reservationId, OnCompleteListener<Void> onCompleteListener) {

    }

    @Override
    public void getById(String reservationId, OnCompleteListener<Void> onCompleteListener) {

    }

    @Override
    public void getAllByUser(User user, OnCompleteListener<Void> onCompleteListener) {

    }
}
