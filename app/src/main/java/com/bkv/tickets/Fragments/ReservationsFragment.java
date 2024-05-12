package com.bkv.tickets.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bkv.tickets.Activities.HomeActivity;
import com.bkv.tickets.Adapters.ReservationItemAdapter;
import com.bkv.tickets.Models.Reservation;
import com.bkv.tickets.Models.User;
import com.bkv.tickets.R;
import com.bkv.tickets.Services.FirestoreServices.FirestoreReservationService;
import com.bkv.tickets.Services.Interfaces.IReservationService;

import java.util.ArrayList;
import java.util.List;

public class ReservationsFragment extends Fragment {
    private static final String LOG_TAG = ReservationsFragment.class.getName();

    private HomeActivity homeActivity;
    private User mUser;
    private IReservationService reservationService;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private ArrayList<Reservation> mReservationList;
    private ReservationItemAdapter mAdapter;


    public ReservationsFragment() {
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
        return inflater.inflate(R.layout.fragment_reservations, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        homeActivity = (HomeActivity) requireActivity();

        if (homeActivity.getFirebaseUser() == null) {
            Log.d(LOG_TAG, "User not logged in");
            homeActivity.finish();
        }

        mUser = homeActivity.getCurrentUser();

        mSwipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(this::onRefresh);

        mRecyclerView = view.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(homeActivity));
        mReservationList = new ArrayList<>();

        mAdapter = new ReservationItemAdapter(homeActivity, mReservationList);
        mRecyclerView.setAdapter(mAdapter);

        reservationService = new FirestoreReservationService(homeActivity.getDb());

        loadData(false);
    }

    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
        loadData(true);
    }

    private void loadData(boolean refresh) {
        reservationService.getAllByUser(homeActivity.getCurrentUser(), task -> {
            if (!task.isSuccessful()) {
                Exception exception = task.getException();
                if (exception == null || exception.getMessage() == null) {
                    exception = new Exception("Failed to query reservations");
                }
                Log.e(LOG_TAG, exception.getMessage());
                Toast.makeText(homeActivity, getString(R.string.error_unexpected), Toast.LENGTH_SHORT).show();
                return;
            }

            List<Reservation> reservations = task.getResult();

            if (reservations == null) {
                Log.d(LOG_TAG, "Received reservation is empty");
                return;
            }

            mReservationList.clear();
            mReservationList.addAll(task.getResult());
            mAdapter.notifyDataSetChanged();
            if (refresh) mSwipeRefreshLayout.setRefreshing(false);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mSwipeRefreshLayout.setRefreshing(true);
        loadData(true);
    }
}