package com.example.intercorptestj.view.datepicker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.intercorptestj.databinding.DialogfragmentDatePickerBinding;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DatePickerDialogFragment extends DialogFragment {

    private DatePickerListener datePickerListener;

    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    private DialogfragmentDatePickerBinding binding;

    public DatePickerDialogFragment(DatePickerListener datePickerListener) {
        this.datePickerListener = datePickerListener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        binding = DialogfragmentDatePickerBinding.inflate(LayoutInflater.from(getContext()));
        View view = binding.getRoot();

        DatePicker datePicker = binding.datePicker;
        datePicker.setMaxDate(System.currentTimeMillis());

        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setPositiveButton("Accept", (dialogInterface, i) -> {
                    try {
                        Date date =
                                sdf.parse(datePicker.getDayOfMonth() + "/" + datePicker.getMonth() + 1 + "/" + datePicker.getYear());
                        datePickerListener.onSelected(date);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                })
                .setNegativeButton("Cancel", (dialogInterface, i) -> {
                    this.dismiss();
                })
                .setView(view)
                .create();

        return alertDialog;
    }

    public interface DatePickerListener {
        void onSelected(Date date);
    }
}


