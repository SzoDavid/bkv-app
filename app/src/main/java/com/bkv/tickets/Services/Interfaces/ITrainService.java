package com.bkv.tickets.Services.Interfaces;

import androidx.annotation.NonNull;

import com.bkv.tickets.Models.RailLine;
import com.bkv.tickets.Models.Station;
import com.bkv.tickets.Models.TimeTableElement;
import com.bkv.tickets.Models.Train;
import com.google.android.gms.tasks.OnCompleteListener;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;

public interface ITrainService {
    void getById(@NonNull String id, @NonNull OnCompleteListener<Void> onCompleteListener);
    void getAllByRailLineAndDirection(@NonNull RailLine line, boolean ascendingDirection, @NonNull OnCompleteListener<Void> onCompleteListener);
    List<TimeTableElement> calculateTimetable(@NonNull Train train, @NonNull Station start, @NonNull Station destination);
    List<TimeTableElement> calculateTimetable(@NonNull Train train);
}
