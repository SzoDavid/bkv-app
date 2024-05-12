package com.bkv.tickets.Services.FirestoreServices;

import android.util.Log;

import com.bkv.tickets.Models.Reservation;
import com.bkv.tickets.Models.ReservationSaveObject;
import com.bkv.tickets.Models.Station;
import com.bkv.tickets.Models.Train;
import com.bkv.tickets.Models.User;
import com.bkv.tickets.Services.Interfaces.IReservationService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class FirestoreReservationService implements IReservationService {
    private static final String LOG_TAG = FirestoreReservationService.class.getName();

    public static final String RESERVATIONS_COLLECTION_PATH = "reservations";
    public static final String FROM_STATION_FIELD = "from.station";
    public static final String FROM_STATION_NAME_FIELD = "from.stationName";
    public static final String TO_STATION_FIELD = "to.station";
    public static final String TO_STATION_NAME_FIELD = "to.stationName";
    public static final String TRAIN_FIELD = "train";
    public static final String TRAIN_NAME_FIELD = "trainName";
    public static final String DEPART_TIME_FIELD = "departTime";
    public static final String USER_FIELD = "user";

    private final FirebaseFirestore db;

    public FirestoreReservationService(FirebaseFirestore db) {
        this.db = db;
    }

    @Override
    public void create(Reservation reservation, OnCompleteListener<DocumentReference> onCompleteListener) {
        ReservationSaveObject reservationSaveObject = new ReservationSaveObject();
        LocalDateTime departure = reservation.getTrain().getDeparture();
        reservationSaveObject.setDepartTime(new Timestamp(departure.toInstant(ZoneOffset.systemDefault().getRules().getOffset(departure))));

        Map<String, Object> fromMap = new HashMap<>();
        fromMap.put("station", db.collection(FirestoreStationService.STATION_COLLECTION_PATH).document(reservation.getFrom().getId()));
        fromMap.put("stationName", reservation.getFrom().getName());
        reservationSaveObject.setFrom(fromMap);

        Map<String, Object> toMap = new HashMap<>();
        toMap.put("station", db.collection(FirestoreStationService.STATION_COLLECTION_PATH).document(reservation.getTo().getId()));
        toMap.put("stationName", reservation.getTo().getName());
        reservationSaveObject.setTo(toMap);

        reservationSaveObject.setTrain(reservation.getTrain().getReference());
        reservationSaveObject.setTrainName(reservation.getTrain().getName());
        reservationSaveObject.setUser(db.collection(FirestoreUserService.USERS_COLLECTION_PATH).document(reservation.getUser().getId()));

        db.collection(RESERVATIONS_COLLECTION_PATH)
                .add(reservationSaveObject)
                .addOnCompleteListener(onCompleteListener);
    }

    @Override
    public void delete(String reservationId, OnCompleteListener<Void> onCompleteListener) {
        db.collection(RESERVATIONS_COLLECTION_PATH)
                .document(reservationId)
                .delete()
                .addOnCompleteListener(onCompleteListener);
    }

    @Override
    public void getById(String reservationId, OnCompleteListener<Reservation> onCompleteListener) {
        db.collection(RESERVATIONS_COLLECTION_PATH)
                .document(reservationId)
                .get()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Exception exception = task.getException();
                        if (exception == null) {
                            exception = new Exception("Failed to get reservation");
                        }

                        Log.e(LOG_TAG, exception.toString());
                        onCompleteListener.onComplete(Tasks.forException(exception));
                        return;
                    }

                    try {
                        Reservation reservation = parseReservation(task.getResult());

                        reservation.getUser().getReference().get().addOnSuccessListener(userDoc -> {
                            try {
                                reservation.setUser(FirestoreUserService.parseUser(userDoc));
                            } catch (IllegalStateException | NullPointerException e) {
                                onCompleteListener.onComplete(Tasks.forException(e));
                            }
                            reservation.getTrain().getReference().get().addOnSuccessListener(trainDoc -> {
                                try {
                                    reservation.setTrain(FirestoreTrainService.parseTrain(trainDoc));
                                } catch (IllegalStateException | NullPointerException e) {
                                    onCompleteListener.onComplete(Tasks.forException(e));
                                }

                                reservation.getTrain().getLine().getReference().get().addOnSuccessListener(lineDoc -> {
                                    try {
                                        reservation.getTrain().setLine(FirestoreRailLineService.parseRailLine(lineDoc));
                                        onCompleteListener.onComplete(Tasks.forResult(reservation));
                                    } catch (IllegalStateException | NullPointerException e) {
                                        onCompleteListener.onComplete(Tasks.forException(e));
                                    }
                                }).addOnFailureListener(error -> onCompleteListener.onComplete(Tasks.forException(error)));
                            }).addOnFailureListener(error -> onCompleteListener.onComplete(Tasks.forException(error)));
                        }).addOnFailureListener(error -> onCompleteListener.onComplete(Tasks.forException(error)));
                    } catch (IllegalStateException | NullPointerException e) {
                        onCompleteListener.onComplete(Tasks.forException(e));
                    }
                });
    }

    @Override
    public void getAllByUser(User user, OnCompleteListener<List<Reservation>> onCompleteListener) {
        db.collection(RESERVATIONS_COLLECTION_PATH)
                .whereEqualTo(USER_FIELD, db.collection(FirestoreUserService.USERS_COLLECTION_PATH).document(user.getId()))
                .get()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Exception exception = task.getException();
                        if (exception == null) {
                            exception = new Exception("Failed to get reservation");
                        }

                        Log.e(LOG_TAG, exception.toString());
                        onCompleteListener.onComplete(Tasks.forException(exception));
                        return;
                    }

                    QuerySnapshot reservationQuery = task.getResult();

                    if (reservationQuery == null || reservationQuery.isEmpty()) {
                        Log.d(LOG_TAG, "Returned query is empty");
                        onCompleteListener.onComplete(Tasks.forResult(null));
                        return;
                    }

                    try {
                        List<Reservation> reservations = new ArrayList<>();

                        for (DocumentSnapshot reservationDocument: reservationQuery.getDocuments()) {
                            reservations.add(parseReservation(reservationDocument));
                        }

                        onCompleteListener.onComplete(Tasks.forResult(reservations));
                    } catch (IllegalStateException e) {
                        onCompleteListener.onComplete(Tasks.forException(e));
                    }
                });
    }

    private Reservation parseReservation(DocumentSnapshot reservationDocument) throws IllegalStateException {
        if (!reservationDocument.exists()) {
            return null;
        }

        Reservation reservation;

        try {
            reservation = new Reservation()
                    .setId(reservationDocument.getId())
                    .setFrom(new Station()
                            .setId(reservationDocument.getDocumentReference(FROM_STATION_FIELD).getId())
                            .setName(reservationDocument.getString(FROM_STATION_NAME_FIELD)))
                    .setTo(new Station()
                            .setId(reservationDocument.getDocumentReference(TO_STATION_FIELD).getId())
                            .setName(reservationDocument.getString(TO_STATION_NAME_FIELD)))
                    .setTrain(new Train(reservationDocument.getDocumentReference(TRAIN_FIELD))
                            .setId(reservationDocument.getDocumentReference(TRAIN_FIELD).getId())
                            .setName(reservationDocument.getString(TRAIN_NAME_FIELD))
                            .setDeparture(LocalDateTime.ofInstant(reservationDocument.getTimestamp(DEPART_TIME_FIELD).toInstant(), TimeZone.getDefault().toZoneId())))
                    .setUser(new User(reservationDocument.getDocumentReference(USER_FIELD)));
        } catch (NullPointerException e) {
            Log.e(LOG_TAG, "Null pointer exception in parseReservation");
            throw new IllegalStateException("Failed to parse Reservation", e);
        }

        if (reservation == null || reservation.getId() == null
                || reservation.getFrom().getId() == null || reservation.getFrom().getName() == null
                || reservation.getTo().getId() == null || reservation.getTo().getName() == null
                || reservation.getTrain().getId() == null || reservation.getTrain().getName() == null
                || reservation.getTrain().getDeparture() == null || reservation.getUser().getReference() == null) {
            Log.e(LOG_TAG, "Parsing is not complete in parseReservation");
            throw new IllegalStateException("Failed to parse Reservation");
        }

        return reservation;
    }
}