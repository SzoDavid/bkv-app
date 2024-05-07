package com.bkv.tickets.Activities;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bkv.tickets.Adapters.ReservationItemAdapter;
import com.bkv.tickets.Models.RailLine;
import com.bkv.tickets.Models.Reservation;
import com.bkv.tickets.Models.Station;
import com.bkv.tickets.Models.Train;
import com.bkv.tickets.Models.TravelTime;
import com.bkv.tickets.Models.User;
import com.bkv.tickets.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

public class ReservationsActivity extends AppCompatActivity {
    private static final String LOG_TAG = ReservationsActivity.class.getName();
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private CollectionReference mReservationsCollection;

    private RecyclerView mRecyclerView;
    private ArrayList<Reservation> mReservationList;
    private ReservationItemAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reservations);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Log.d(LOG_TAG, "Success");
        } else {
            Log.d(LOG_TAG, "Not success");
            finish();
        }

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mReservationList = new ArrayList<>();

        mAdapter = new ReservationItemAdapter(this, mReservationList);
        mRecyclerView.setAdapter(mAdapter);

        mFirestore = FirebaseFirestore.getInstance();
        mReservationsCollection = mFirestore.collection("reservations");
        loadData();
    }

    private void loadData() {
        mReservationList.clear();

        mReservationsCollection.orderBy("departTime").limit(5).get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                Reservation reservation = document.toObject(Reservation.class);


                mReservationList.add(reservation);
            }

            mAdapter.notifyDataSetChanged();
        });

//        for (int i = 0; i < 10; i++) {
//            mReservationList.add(new Reservation(
//                    "id" + i,
//                    new Station("id", "stationA" + i),
//                    new Station("id", "stationB" + i),
//                    new Train("id", "train" + i, 100, 100,
//                            new RailLine("id", new ArrayList<Station>(), new ArrayList<TravelTime>()), true, LocalDateTime.now()),
//                    new User("id", "id", "email", "name", new Date())));
//        }

    }
}