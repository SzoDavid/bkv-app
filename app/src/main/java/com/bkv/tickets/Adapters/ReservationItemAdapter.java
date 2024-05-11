package com.bkv.tickets.Adapters;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bkv.tickets.Activities.ReservationActivity;
import com.bkv.tickets.Models.Reservation;
import com.bkv.tickets.R;
import com.bkv.tickets.Services.PropertiesService;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ReservationItemAdapter extends RecyclerView.Adapter<ReservationItemAdapter.ViewHolder>{
    private ArrayList<Reservation> mReservationItems;
    private Context mContext;
    private int lastPosition = -1;


    public ReservationItemAdapter(Context context, ArrayList<Reservation> items) {
        this.mReservationItems = items;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.reservation_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ReservationItemAdapter.ViewHolder holder, int position) {
        Reservation currentItem = mReservationItems.get(position);
        holder.bindTo(currentItem);

        if (holder.getBindingAdapterPosition() > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_row);
            holder.itemView.startAnimation(animation);
            lastPosition = holder.getBindingAdapterPosition();
        }
    }

    @Override
    public int getItemCount() {
        return mReservationItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final String LOG_TAG = ViewHolder.class.getName();

        private TextView mTrainTextView;
        private TextView mFromToTextView;
        private TextView mTimeTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mTrainTextView = itemView.findViewById(R.id.trainTextView);
            mFromToTextView = itemView.findViewById(R.id.fromToTextView);
            mTimeTextView = itemView.findViewById(R.id.departTimeTextView);
        }

        public void bindTo(Reservation currentItem) {
            mTrainTextView.setText(currentItem.getTrain().getName());
            mFromToTextView.setText(String.format("%s -> %s",currentItem.getFrom().getName(), currentItem.getTo().getName()));
            mTimeTextView.setText(currentItem.getTrain().getDeparture().format(DateTimeFormatter.ofPattern("yyyy.MM.dd. HH:mm")));

            itemView.findViewById(R.id.reservationDetailsButton).setOnClickListener(view -> {
                Intent intent = new Intent(mContext, ReservationActivity.class);
                intent.putExtra("SECRET_KEY", PropertiesService.getSecretKey());
                intent.putExtra("RESERVATION_ID", currentItem.getId());
                startActivity(mContext, intent, new Bundle());
            });
        }
    }

}
