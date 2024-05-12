package com.bkv.tickets.Activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bkv.tickets.Adapters.StopItemAdapter;
import com.bkv.tickets.Models.Reservation;
import com.bkv.tickets.Models.TimeTableElement;
import com.bkv.tickets.R;
import com.bkv.tickets.Services.FirestoreServices.FirestoreReservationService;
import com.bkv.tickets.Services.Interfaces.IReservationService;
import com.bkv.tickets.Services.Interfaces.ITrainService;
import com.bkv.tickets.Services.PropertiesService;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ReservationActivity extends AppCompatActivity {
    private static final String LOG_TAG = ReservationActivity.class.getName();

    private FirebaseFirestore mDB;
    private IReservationService mReservationService;
    private StopItemAdapter mAdapter;

    private ArrayList<TimeTableElement> mTimeTableElements;
    private Reservation mReservation;

    private TextView mFromToTV;
    private TextView mDepartTV;
    private TextView mNameTV;
    private TextView mTrainNameTV;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reservation);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        String key = getIntent().getStringExtra("SECRET_KEY");
        String reservationId = getIntent().getStringExtra("RESERVATION_ID");
        if (!PropertiesService.getSecretKey().equals(key) || reservationId == null || reservationId.isEmpty()) {
            finish();
        }

        mDB = FirebaseFirestore.getInstance();
        mReservationService = new FirestoreReservationService(mDB);

        mFromToTV = findViewById(R.id.fromToTextView);
        mDepartTV = findViewById(R.id.departTextView);
        mNameTV = findViewById(R.id.nameTextView);
        mTrainNameTV = findViewById(R.id.trainNameTextView);
        mRecyclerView = findViewById(R.id.stopRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mTimeTableElements = new ArrayList<>();
        mAdapter = new StopItemAdapter(this, mTimeTableElements);
        mRecyclerView.setAdapter(mAdapter);

        mReservationService.getById(reservationId, this::initData);
    }

    public void initData(Task<Reservation> task) {
        if (!task.isSuccessful() || task.getResult() == null) {
            Exception exception = task.getException();
            if (exception == null || exception.getMessage() == null) {
                exception = new Exception("Failed to query reservations");
            }
            Log.e(LOG_TAG, exception.getMessage());
            Toast.makeText(this, getString(R.string.error_unexpected), Toast.LENGTH_SHORT).show();
            return;
        }

        mReservation = task.getResult();

        mFromToTV.setText(String.format("%s -> %s", mReservation.getFrom().getName(), mReservation.getTo().getName()));
        mDepartTV.setText(mReservation.getTrain().getDeparture().format(DateTimeFormatter.ofPattern("yyyy.MM.dd. HH:mm")));
        mTrainNameTV.setText(mReservation.getTrain().getName());
        mNameTV.setText(mReservation.getUser().getName());
        mTimeTableElements.addAll(ITrainService.calculateTimetable(
                mReservation.getTrain(),
                mReservation.getFrom(),
                mReservation.getTo()));

        mAdapter.notifyDataSetChanged();
    }

    public void backOnClickListener(View view) {
        finish();
    }

    public void deleteOnClickListener(View view) {
        mReservationService.delete(mReservation.getId(), task -> {
            if (!task.isSuccessful()) {
                Exception exception = task.getException();
                if (exception == null || exception.getMessage() == null) {
                    exception = new Exception("Failed to query reservations");
                }
                Log.e(LOG_TAG, exception.getMessage());
                Toast.makeText(this, getString(R.string.error_unexpected), Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(this, getString(R.string.success_delete_reservation), Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}