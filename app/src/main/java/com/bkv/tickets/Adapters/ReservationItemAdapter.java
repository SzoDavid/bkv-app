package com.bkv.tickets.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bkv.tickets.Models.Reservation;
import com.bkv.tickets.R;

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

        if (holder.getAdapterPosition() > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_row);
            holder.itemView.startAnimation(animation);
            lastPosition = holder.getAdapterPosition();
        }
    }

    @Override
    public int getItemCount() {
        return mReservationItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mFromTextView;
        private TextView mToTextView;
        private TextView mDateTextView;
        private TextView mTimeTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mFromTextView = itemView.findViewById(R.id.fromTextView);
            mToTextView = itemView.findViewById(R.id.toTextView);
            mDateTextView = itemView.findViewById(R.id.dateTextView);
            mTimeTextView = itemView.findViewById(R.id.timeTextView);

            itemView.findViewById(R.id.reservationDetailsButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("Activity", "Details click");
                }
            });
        }

        public void bindTo(Reservation currentItem) {
            mFromTextView.setText(currentItem.getFrom().getName());
            mToTextView.setText(currentItem.getTo().getName());
            mDateTextView.setText(currentItem.getTrain().getDeparture().toLocalDate().toString());
            mTimeTextView.setText(currentItem.getTrain().getDeparture().toLocalTime().toString());
        }
    }

}
