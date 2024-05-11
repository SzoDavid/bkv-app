package com.bkv.tickets.Services.FirestoreServices;

import android.util.Log;

import com.bkv.tickets.Models.Station;
import com.bkv.tickets.Services.Interfaces.IStationService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FirestoreStationService implements IStationService {
    private static final String LOG_TAG = FirestoreStationService.class.getName();
    private final FirebaseFirestore db;

    public static final String STATION_COLLECTION_PATH = "stations";
    public static final String NAME_FIELD = "name";

    public FirestoreStationService(FirebaseFirestore db) {
        this.db = db;
    }

    @Override
    public void getAll(OnCompleteListener<List<Station>> onCompleteListener) {
        db.collection(STATION_COLLECTION_PATH)
                .orderBy(NAME_FIELD)
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

                    QuerySnapshot stationQuery = task.getResult();

                    if (stationQuery == null || stationQuery.isEmpty()) {
                        Log.d(LOG_TAG, "Returned query is empty");
                        onCompleteListener.onComplete(Tasks.forResult(null));
                        return;
                    }

                    try {
                        List<Station> stations = new ArrayList<>();

                        for (DocumentSnapshot stationDocument: stationQuery.getDocuments()) {
                            stations.add(parseStation(stationDocument));
                        }

                        onCompleteListener.onComplete(Tasks.forResult(stations));
                    } catch (IllegalStateException e) {
                        onCompleteListener.onComplete(Tasks.forException(e));
                    }
                });
    }

    public static Station parseStation(DocumentSnapshot stationDocument) throws IllegalStateException {
        if (stationDocument == null || !stationDocument.exists()) {
            return null;
        }

        Station station = stationDocument.toObject(Station.class);

        if (station == null || station.getName() == null) {
            Log.e(LOG_TAG, "toObjectReturned null");
            throw new IllegalStateException("Failed to parse user");
        }

        station.setId(stationDocument.getId());
        return station;
    }
}
