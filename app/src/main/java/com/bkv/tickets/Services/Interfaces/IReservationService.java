package com.bkv.tickets.Services.Interfaces;

import com.bkv.tickets.Models.Reservation;
import com.bkv.tickets.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.DocumentReference;

import java.util.List;

public interface IReservationService {
    void create(Reservation reservation, OnCompleteListener<DocumentReference> onCompleteListener);
    void delete(String reservationId, OnCompleteListener<Void> onCompleteListener);
    void getById(String reservationId, OnCompleteListener<Reservation> onCompleteListener);
    void getAllByUser(User user, OnCompleteListener<List<Reservation>> onCompleteListener);
}
