package com.bkv.tickets.Services.FirestoreServices;

import android.util.Log;

import androidx.annotation.NonNull;

import com.bkv.tickets.Models.RailLine;
import com.bkv.tickets.Models.Station;
import com.bkv.tickets.Models.Stop;
import com.bkv.tickets.Services.Interfaces.IRailLineService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FirestoreRailLineService implements IRailLineService {
    private static final String LOG_TAG = FirestoreRailLineService.class.getName();
    private FirebaseFirestore mDB;

    public static final String RAIL_LINE_COLLECTION_PATH = "railLines";
    public static final String STATIONS_FIELD = "stations";
    public static final String DURATION_MINUTES_FIELD = "durationMinutes";
    public static final String STATION_FIELD = "station";
    public static final String STATION_NAME_FIELD = "stationName";

    public FirestoreRailLineService(FirebaseFirestore mDB) {
        this.mDB = mDB;
    }

    @Override
    public void getById(@NonNull String railLineId, @NonNull OnCompleteListener<RailLine> onCompleteListener) {
        mDB.collection(RAIL_LINE_COLLECTION_PATH)
                .document(railLineId)
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
                        onCompleteListener.onComplete(Tasks.forResult(parseRailLine(task.getResult())));
                    } catch (IllegalStateException | NullPointerException e) {
                        onCompleteListener.onComplete(Tasks.forException(e));
                    }
                });
    }

    @Override
    public void getAllByStartAndDestinationStations(@NonNull Station start, @NonNull Station destination, @NonNull OnCompleteListener<Void> onCompleteListener) {
        //TODO
    }

    public static RailLine parseRailLine(DocumentSnapshot railLineDocument) throws IllegalStateException {
        if (railLineDocument == null || !railLineDocument.exists()) {
            return null;
        }

        RailLine railLine;

        try {
            railLine = new RailLine()
                    .setId(railLineDocument.getId())
                    .setStations(new ArrayList<>());

            List<Map<String, Object>> stationMaps = (List<Map<String, Object>>) railLineDocument.get(STATIONS_FIELD);
            for (Map<String, Object> stationMap : stationMaps) {
                railLine.getStations().add(new Stop(
                        new Station(
                                ((DocumentReference) stationMap.get(STATION_FIELD)).getId(),
                                (String) stationMap.get(STATION_NAME_FIELD)),
                        (Long) stationMap.get(DURATION_MINUTES_FIELD)));

            }
        } catch (NullPointerException e) {
            Log.e(LOG_TAG, "Null pointer exception in parseRailLine");
            throw new IllegalStateException("Failed to parse rail line", e);
        }

        if (railLine == null || railLine.getId() == null || railLine.getStations() == null
                || railLine.getStations().isEmpty()) {
            Log.e(LOG_TAG, "Parsing is not complete in parseRailLine");
            throw new IllegalStateException("Failed to parse rail line");
        }

        return railLine;
    }
}
