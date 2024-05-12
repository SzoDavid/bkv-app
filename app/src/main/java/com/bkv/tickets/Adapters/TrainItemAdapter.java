package com.bkv.tickets.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bkv.tickets.Models.Reservation;
import com.bkv.tickets.Models.TrainSearchResult;
import com.bkv.tickets.Models.User;
import com.bkv.tickets.R;
import com.bkv.tickets.Services.Interfaces.IReservationService;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class TrainItemAdapter extends RecyclerView.Adapter<TrainItemAdapter.ViewHolder> {

    private ArrayList<TrainSearchResult> mTrainItems;
    private Context mContext;
    private User user;
    private IReservationService reservationService;
    private int lastPosition = -1;

    public TrainItemAdapter(Context mContext, ArrayList<TrainSearchResult> mTrainItems, User user, IReservationService reservationService) {
        this.mTrainItems = mTrainItems;
        this.mContext = mContext;
        this.user = user;
        this.reservationService = reservationService;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.train_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TrainSearchResult currentItem = mTrainItems.get(position);
        holder.bindTo(currentItem);

        if (holder.getBindingAdapterPosition() > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_row);
            holder.itemView.startAnimation(animation);
            lastPosition = holder.getBindingAdapterPosition();
        }
    }

    @Override
    public int getItemCount() {
        return mTrainItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final String LOG_TAG = TrainItemAdapter.ViewHolder.class.getName();

        private TextView mTrainTextView;
        private TextView mFromToTextView;
        private TextView mDepartTimeTextView;
        private TextView mArriveTimeTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mTrainTextView = itemView.findViewById(R.id.trainTextView);
            mFromToTextView = itemView.findViewById(R.id.fromToTextView);
            mDepartTimeTextView = itemView.findViewById(R.id.departTimeTextView);
            mArriveTimeTextView = itemView.findViewById(R.id.arriveTimeTextView);
        }

        public void bindTo(TrainSearchResult currentItem) {
            mTrainTextView.setText(currentItem.getTrain().getName());
            mFromToTextView.setText(String.format("%s -> %s",currentItem.getStart().getStation().getName(), currentItem.getDestination().getStation().getName()));
            mDepartTimeTextView.setText(currentItem.getStart().getDepartureTime().format(DateTimeFormatter.ofPattern("yyyy.MM.dd. HH:mm")));
            mArriveTimeTextView.setText(currentItem.getDestination().getDepartureTime().format(DateTimeFormatter.ofPattern("HH:mm")));

            itemView.findViewById(R.id.addButton).setOnClickListener(view -> {
                Reservation reservation = new Reservation()
                        .setFrom(currentItem.getStart().getStation())
                        .setTo(currentItem.getDestination().getStation())
                        .setTrain(currentItem.getTrain())
                        .setUser(user);

                reservationService.create(reservation, task -> {
                    if (!task.isSuccessful()) {
                        Exception exception = task.getException();
                        if (exception == null || exception.getMessage() == null) {
                            exception = new Exception("Failed to query reservations");
                        }
                        Log.e(LOG_TAG, exception.getMessage());
                        Toast.makeText(mContext, mContext.getString(R.string.error_unexpected), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Toast.makeText(mContext, mContext.getString(R.string.success_save_reservation), Toast.LENGTH_SHORT).show();
                });
            });
        }
    }
}
