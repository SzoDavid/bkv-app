package com.bkv.tickets.Services.FirestoreServices;

import android.util.Log;

import androidx.annotation.NonNull;

import com.bkv.tickets.Models.RailLine;
import com.bkv.tickets.Models.Train;
import com.bkv.tickets.Services.Interfaces.ITrainService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDateTime;
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
    public void getById(@NonNull String id, @NonNull OnCompleteListener<Void> onCompleteListener) {
        //TODO
    }

    @Override
    public void getAllByRailLineAndDirection(@NonNull RailLine line, boolean ascendingDirection, @NonNull OnCompleteListener<Void> onCompleteListener) {
        //TODO
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
