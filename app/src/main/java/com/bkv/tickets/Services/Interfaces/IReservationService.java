package com.bkv.tickets.Services.Interfaces;

import com.bkv.tickets.Models.Reservation;
import com.bkv.tickets.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;

public interface IReservationService {
    void create(Reservation reservation, OnCompleteListener<Void> onCompleteListener);
    void delete(String reservationId, OnCompleteListener<Void> onCompleteListener);
    void getById(String reservationId, OnCompleteListener<Void> onCompleteListener);
    void getAllByUser(User user, OnCompleteListener<Void> onCompleteListener);
}
