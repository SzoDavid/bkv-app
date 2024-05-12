package com.bkv.tickets.Services.FirestoreServices;

import android.util.Log;

import androidx.annotation.NonNull;

import com.bkv.tickets.Models.RailLine;
import com.bkv.tickets.Models.Train;
import com.bkv.tickets.Services.Interfaces.ITrainService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class FirestoreTrainService implements ITrainService {
    private static final String LOG_TAG = FirestoreTrainService.class.getName();
    private final FirebaseFirestore db;

    public static final String TRAINS_COLLECTION_PATH = "trains";
    public static final String NAME_FIELD = "name";
    public static final String ASCENDING_DIRECTION_FIELD = "ascendingDirection";
    public static final String DEPART_TIME_FIELD = "departureTime";
    public static final String LINE_FIELD = "line";


    public FirestoreTrainService(FirebaseFirestore db) {
        this.db = db;
    }

    @Override
    public void getAllByRailLineDirectionAndDate(@NonNull RailLine line, boolean ascendingDirection, LocalDate date, @NonNull OnCompleteListener<List<Train>> onCompleteListener) {
        LocalDateTime startDateTime = date.atStartOfDay();
        LocalDateTime endDateTime = LocalTime.MAX.atDate(date);
        Date startDate = Date.from(startDateTime.toInstant(ZoneOffset.systemDefault().getRules().getOffset(startDateTime)));
        Date endDate = Date.from(endDateTime.toInstant(ZoneOffset.systemDefault().getRules().getOffset(endDateTime)));

        db.collection(TRAINS_COLLECTION_PATH)
                .whereGreaterThanOrEqualTo(DEPART_TIME_FIELD, startDate)
                .whereLessThanOrEqualTo(DEPART_TIME_FIELD, endDate)
                .whereEqualTo(ASCENDING_DIRECTION_FIELD, ascendingDirection)
                .whereEqualTo(LINE_FIELD, db.collection(FirestoreRailLineService.RAIL_LINE_COLLECTION_PATH).document(line.getId()))
                .orderBy(DEPART_TIME_FIELD)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    ArrayList<Train> trains = new ArrayList<>();

                    try {
                        for (QueryDocumentSnapshot trainDocument : queryDocumentSnapshots) {
                            trains.add(parseTrain(trainDocument));
                        }
                    } catch (IllegalStateException e) {
                        onCompleteListener.onComplete(Tasks.forException(e));
                    }

                    onCompleteListener.onComplete(Tasks.forResult(trains));
                }).addOnFailureListener(e -> onCompleteListener.onComplete(Tasks.forException(e)));
    }

    public static Train parseTrain(DocumentSnapshot trainDocument) {
        if (trainDocument == null || !trainDocument.exists()) {
            return null;
        }

        Train train;

        try {
            train = new Train()
                    .setReference(trainDocument.getReference())
                    .setId(trainDocument.getId())
                    .setName(trainDocument.getString(NAME_FIELD))
                    .setAscendingOrder(Boolean.TRUE.equals(trainDocument.getBoolean(ASCENDING_DIRECTION_FIELD)))
                    .setDeparture(LocalDateTime.ofInstant(trainDocument.getTimestamp(DEPART_TIME_FIELD).toInstant(), TimeZone.getDefault().toZoneId()))
                    .setLine(new RailLine(trainDocument.getDocumentReference(LINE_FIELD))
                            .setId(trainDocument.getDocumentReference(LINE_FIELD).getId()));
        } catch (NullPointerException e) {
            Log.e(LOG_TAG, "Null pointer exception in parseTrain");
            throw new IllegalStateException("Failed to parse train", e);
        }

        if (train == null || train.getId() == null || train.getName() == null
                || train.getDeparture() == null || train.getLine() == null
                || train.getLine().getReference() == null || train.getLine().getId() == null) {
            Log.e(LOG_TAG, "Parsing is not complete in parseTrain");
            throw new IllegalStateException("Failed to parse train");
        }

        return train;
    }
}
