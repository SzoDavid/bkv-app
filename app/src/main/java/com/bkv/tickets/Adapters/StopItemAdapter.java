package com.bkv.tickets.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bkv.tickets.Models.TimeTableElement;
import com.bkv.tickets.R;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class StopItemAdapter extends RecyclerView.Adapter<StopItemAdapter.ViewHolder> {
    private ArrayList<TimeTableElement> mTimeTableElements;
    private Context mContext;
    private int lastPosition = -1;

    public StopItemAdapter(Context mContext, ArrayList<TimeTableElement> mTimeTableElements) {
        this.mTimeTableElements = mTimeTableElements;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.stop_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TimeTableElement currentItem = mTimeTableElements.get(position);
        holder.bindTo(currentItem);

        if (holder.getBindingAdapterPosition() > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_row);
            holder.itemView.startAnimation(animation);
            lastPosition = holder.getBindingAdapterPosition();
        }
    }

    @Override
    public int getItemCount() {
        return mTimeTableElements.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final String LOG_TAG = StopItemAdapter.ViewHolder.class.getName();

        private TextView mStationTextView;
        private TextView mArrivalTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mStationTextView = itemView.findViewById(R.id.stationTextView);
            mArrivalTextView = itemView.findViewById(R.id.arrivalTextView);
        }

        public void bindTo(TimeTableElement currentItem) {
            mStationTextView.setText(currentItem.getStation().getName());
            mArrivalTextView.setText(currentItem.getDepartureTime().format(DateTimeFormatter.ofPattern("HH:mm")));
        }
    }
}
