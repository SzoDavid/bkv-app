package com.bkv.tickets.Fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.time.LocalDate;
import java.util.Calendar;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private OnDateSetListener onDateSetListener;
    protected LocalDate selectedDate;

    public interface OnDateSetListener {
        void onDateSet(LocalDate date);
    }

    public DatePickerFragment(LocalDate selectedDate) {
        this.selectedDate = selectedDate;
    }

    public DatePickerFragment setOnDateSetListener(OnDateSetListener listener) {
        onDateSetListener = listener;
        return this;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        int year = selectedDate.getYear();
        int month = selectedDate.getMonthValue() - 1;
        int day = selectedDate.getDayOfMonth();

        return new DatePickerDialog(requireContext(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        selectedDate = LocalDate.of(year, month + 1, dayOfMonth);
        if (onDateSetListener != null) {
            onDateSetListener.onDateSet(selectedDate);
        }
    }
}
