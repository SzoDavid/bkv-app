package com.bkv.tickets.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bkv.tickets.Activities.HomeActivity;
import com.bkv.tickets.Adapters.StationSpinnerAdapter;
import com.bkv.tickets.Adapters.TrainItemAdapter;
import com.bkv.tickets.Models.RailLine;
import com.bkv.tickets.Models.Station;
import com.bkv.tickets.Models.Stop;
import com.bkv.tickets.Models.TimeTableElement;
import com.bkv.tickets.Models.Train;
import com.bkv.tickets.Models.TrainSearchResult;
import com.bkv.tickets.Models.User;
import com.bkv.tickets.R;
import com.bkv.tickets.Services.FirestoreServices.FirestoreRailLineService;
import com.bkv.tickets.Services.FirestoreServices.FirestoreReservationService;
import com.bkv.tickets.Services.FirestoreServices.FirestoreStationService;
import com.bkv.tickets.Services.FirestoreServices.FirestoreTrainService;
import com.bkv.tickets.Services.Interfaces.IRailLineService;
import com.bkv.tickets.Services.Interfaces.IStationService;
import com.bkv.tickets.Services.Interfaces.ITrainService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {
    private static final String LOG_TAG = SearchFragment.class.getName();

    private HomeActivity homeActivity;
    private User mUser;
    private IStationService stationService;
    private IRailLineService railLineService;
    private ITrainService trainService;
    private DatePickerFragment datePickerFragment;

    private Spinner fromSpinner;
    private Spinner toSpinner;
    private TextView dateTV;
    private RecyclerView recyclerView;
    private ArrayList<TrainSearchResult> trainSearchResults;
    private TrainItemAdapter trainItemAdapter;

    private LocalDate searchDate;
    private Station fromStation;
    private Station toStation;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        homeActivity = (HomeActivity) requireActivity();

        if (homeActivity.getFirebaseUser() == null) {
            Log.d(LOG_TAG, "User not logged in");
            homeActivity.finish();
        }

        stationService = new FirestoreStationService(homeActivity.getDb());
        railLineService = new FirestoreRailLineService(homeActivity.getDb());
        trainService = new FirestoreTrainService(homeActivity.getDb());

        mUser = homeActivity.getCurrentUser();

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(homeActivity));
        trainSearchResults = new ArrayList<>();
        trainItemAdapter = new TrainItemAdapter(homeActivity, trainSearchResults, mUser, new FirestoreReservationService(homeActivity.getDb()));
        recyclerView.setAdapter(trainItemAdapter);

        searchDate = LocalDate.now();
        datePickerFragment = new DatePickerFragment(searchDate)
                .setOnDateSetListener(date -> {
                    searchDate = date;
                    dateTV.setText(searchDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd.")));
                });

        fromSpinner = view.findViewById(R.id.fromSpinner);
        toSpinner = view.findViewById(R.id.toSpinner);
        dateTV = view.findViewById(R.id.dateTextView);
        dateTV.setText(searchDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd.")));

        view.findViewById(R.id.datePickerButton).setOnClickListener(this::datePickerOnClickListener);
        view.findViewById(R.id.searchButton).setOnClickListener(this::searchOnClickListener);

        loadData();
    }

    public void datePickerOnClickListener(View view) {
        datePickerFragment.show(homeActivity.getSupportFragmentManager(), "datePicker");
    }

    public void searchOnClickListener(View view) {
        trainSearchResults.clear();
        trainItemAdapter.notifyDataSetChanged();

        if (fromStation == null || toStation == null) {
            return;
        }

        if (fromStation == toStation) {
            Toast.makeText(homeActivity, getString(R.string.error_stations_equal), Toast.LENGTH_SHORT).show();
            return;
        }

        railLineService.getAllByStartAndDestinationStations(fromStation, toStation, task -> {
            if (!task.isSuccessful()) {
                Exception exception = task.getException();
                if (exception == null || exception.getMessage() == null) {
                    exception = new Exception("Failed to query reservations");
                }
                Log.e(LOG_TAG, exception.getMessage());
                Toast.makeText(homeActivity, getString(R.string.error_unexpected), Toast.LENGTH_SHORT).show();
                return;
            }

            List<RailLine> railLines = task.getResult();

            for (RailLine line : railLines) {
                List<Stop> stations = line.getStations();
                int startIndex = stations.indexOf(new Stop().setStation(fromStation));
                int destinationIndex = stations.indexOf(new Stop().setStation(toStation));

                trainService.getAllByRailLineDirectionAndDate(line, startIndex < destinationIndex, searchDate, trainTask -> {
                    if (!task.isSuccessful()) {
                        Exception exception = task.getException();
                        if (exception == null || exception.getMessage() == null) {
                            exception = new Exception("Failed to query reservations");
                        }
                        Log.e(LOG_TAG, exception.getMessage());
                        Toast.makeText(homeActivity, getString(R.string.error_unexpected), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    for (Train train: trainTask.getResult()) {
                        train.getLine().getReference().get().addOnSuccessListener(lineDoc -> {
                            try {
                                train.setLine(FirestoreRailLineService.parseRailLine(lineDoc));
                                List<TimeTableElement> timeTable = ITrainService.calculateTimetable(train, fromStation, toStation);

                                trainSearchResults.add(new TrainSearchResult(
                                        train,
                                        timeTable.get(0),
                                        timeTable.get(timeTable.size() - 1)
                                ));

                                trainItemAdapter.notifyItemInserted(trainItemAdapter.getItemCount());
                            } catch (IllegalStateException | NullPointerException e) {
                                Log.e(LOG_TAG, e.getMessage());
                            }
                        }).addOnFailureListener(error -> Log.e(LOG_TAG, error.getMessage()));
                    }
                });
            }
        });
    }

    private void loadData() {
        stationService.getAll(task -> {
            if (!task.isSuccessful()) {
                Exception exception = task.getException();
                if (exception == null || exception.getMessage() == null) {
                    exception = new Exception("Failed to query reservations");
                }
                Log.e(LOG_TAG, exception.getMessage());
                Toast.makeText(homeActivity, getString(R.string.error_unexpected), Toast.LENGTH_SHORT).show();
                return;
            }

            List<Station> stationList = task.getResult();
            StationSpinnerAdapter fromAdapter = new StationSpinnerAdapter(homeActivity, stationList);
            StationSpinnerAdapter toAdapter = new StationSpinnerAdapter(homeActivity, stationList);

            fromStation = stationList.get(0);
            toStation = stationList.get(0);

            fromSpinner.setAdapter(fromAdapter);
            toSpinner.setAdapter(toAdapter);

            fromSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    fromStation = (Station) parent.getItemAtPosition(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    fromStation = null;
                }
            });

            toSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    toStation = (Station) parent.getItemAtPosition(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    toStation = null;
                }
            });
        });
    }
}