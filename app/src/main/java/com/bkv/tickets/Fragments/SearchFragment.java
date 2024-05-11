package com.bkv.tickets.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.bkv.tickets.Activities.HomeActivity;
import com.bkv.tickets.Adapters.StationSpinnerAdapter;
import com.bkv.tickets.Models.RailLine;
import com.bkv.tickets.Models.Station;
import com.bkv.tickets.Models.User;
import com.bkv.tickets.R;
import com.bkv.tickets.Services.FirestoreServices.FirestoreRailLineService;
import com.bkv.tickets.Services.FirestoreServices.FirestoreStationService;
import com.bkv.tickets.Services.FirestoreServices.FirestoreTrainService;
import com.bkv.tickets.Services.Interfaces.IRailLineService;
import com.bkv.tickets.Services.Interfaces.IStationService;
import com.bkv.tickets.Services.Interfaces.ITrainService;

import java.util.List;

public class SearchFragment extends Fragment {
    private static final String LOG_TAG = SearchFragment.class.getName();

    private HomeActivity homeActivity;
    private User mUser;
    private IStationService stationService;
    private IRailLineService railLineService;
    private ITrainService trainService;

    private Spinner fromSpinner;
    private Spinner toSpinner;
    private EditText dateET;

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

        fromSpinner = view.findViewById(R.id.fromSpinner);
        toSpinner = view.findViewById(R.id.toSpinner);
        dateET = view.findViewById(R.id.dateEditText);

        view.findViewById(R.id.searchButton).setOnClickListener(this::searchOnClickListener);

        loadData();
    }

    public void searchOnClickListener(View view) {
        if (fromStation == null || toStation == null) {
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

//            for (RailLine line : railLines) {
//
//            }
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