package com.bkv.tickets.Services.FirestoreServices;

import androidx.annotation.NonNull;

import com.bkv.tickets.Exceptions.ServiceException;
import com.bkv.tickets.Models.RailLine;
import com.bkv.tickets.Models.Station;
import com.bkv.tickets.Models.Stop;
import com.bkv.tickets.Models.TimeTableElement;
import com.bkv.tickets.Models.Train;
import com.bkv.tickets.Services.Interfaces.ITrainService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FirestoreTrainService implements ITrainService {
    private final FirebaseFirestore db;

    public FirestoreTrainService(FirebaseFirestore db) {
        this.db = db;
    }

    @Override
    public void getById(@NonNull String id, @NonNull OnCompleteListener<Void> onCompleteListener) {

    }

    @Override
    public void getAllByRailLineAndDirection(@NonNull RailLine line, boolean ascendingDirection, @NonNull OnCompleteListener<Void> onCompleteListener) {

    }

    @Override
    public List<TimeTableElement> calculateTimetable(@NonNull Train train, @NonNull Station start, @NonNull Station destination) {
        List<Stop> stations = train.getLine().getStations();
        List<TimeTableElement> timeTable = new ArrayList<>();

        int startIndex = stations.indexOf(new Stop().setStation(start));
        int destinationIndex = stations.indexOf(new Stop().setStation(destination));

        if (startIndex < destinationIndex != train.isAscendingOrder()) {
            throw new ServiceException();
        }

        if (train.isAscendingOrder()) {
            for (int i = startIndex; i < destinationIndex; i++) {
                Stop stop = stations.get(i);
                timeTable.add(new TimeTableElement(stop.getStation(), train.getDeparture().plusMinutes(stop.getDurationMinutes())));
            }

            return timeTable;
        }

        int lastStopTime = stations.get(stations.size() - 1).getDurationMinutes();

        for (int i = startIndex; i > destinationIndex; i--) {
            Stop stop = stations.get(i);
            timeTable.add(new TimeTableElement(stop.getStation(), train.getDeparture().plusMinutes(lastStopTime - stop.getDurationMinutes())));
        }

        return timeTable;
    }

    @Override
    public List<TimeTableElement> calculateTimetable(@NonNull Train train) {
        List<Stop> stations = train.getLine().getStations();
        return calculateTimetable(
                train,
                stations.get(train.isAscendingOrder() ? 0 : stations.size() - 1).getStation(),
                stations.get(train.isAscendingOrder() ? stations.size() - 1 : 0).getStation());
    }
}
