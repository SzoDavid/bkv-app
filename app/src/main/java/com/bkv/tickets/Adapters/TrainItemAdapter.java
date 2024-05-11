package com.bkv.tickets.Adapters;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bkv.tickets.Models.Train;
import com.bkv.tickets.R;

public class TrainItemAdapter {

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

        public void bindTo(Train currentItem) {
//            mTrainTextView.setText(currentItem.getName());
//            mFromToTextView.setText(String.format("%s -> %s",currentItem.getFrom().getName(), currentItem.getTo().getName()));
//            mDepartTimeTextView.setText(currentItem.getTrain().getDeparture().format(DateTimeFormatter.ofPattern("yyyy.MM.dd. HH:mm")));
//
//            itemView.findViewById(R.id.reservationDetailsButton).setOnClickListener(view -> {
//                Intent intent = new Intent(mContext, ReservationActivity.class);
//                intent.putExtra("SECRET_KEY", PropertiesService.getSecretKey());
//                intent.putExtra("RESERVATION_ID", currentItem.getId());
//                startActivity(mContext, intent, new Bundle());
//            });
        }
    }
}
