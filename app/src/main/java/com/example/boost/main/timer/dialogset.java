package com.example.boost.main.timer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.boost.R;

public class dialogset extends DialogFragment {

    @NonNull
    @Override
    public AlertDialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate( R.layout.fragment_dialogset, null);

        NumberPicker hournumberPicker = dialogView.findViewById(R.id.hourset);
        NumberPicker minnumberPicker = dialogView.findViewById(R.id.minset);
        NumberPicker secnumberPicker = dialogView.findViewById(R.id.secset);

        hournumberPicker.setMinValue(0);
        hournumberPicker.setMaxValue(23);
        minnumberPicker.setMinValue(0);
        minnumberPicker.setMaxValue(59);
        secnumberPicker.setMinValue(0);
        secnumberPicker.setMaxValue(59);

        Button btnSave = dialogView.findViewById(R.id.saveaction);
        Button btnCancel = dialogView.findViewById(R.id.cancelaction);

        btnSave.setOnClickListener(v -> {
            int selectedHours = hournumberPicker.getValue();
            int selectedMinutes = minnumberPicker.getValue();
            int selectedSeconds = secnumberPicker.getValue();

            Bundle result = new Bundle();
            result.putInt("hours", selectedHours);
            result.putInt("minutes", selectedMinutes);
            result.putInt("seconds", selectedSeconds);

            getParentFragmentManager().setFragmentResult("setTimeKey", result);

            dismiss();
        });

        btnCancel.setOnClickListener(v -> dismiss());

        return new AlertDialog.Builder(requireActivity())
                .setView(dialogView)
                .create();
    }
}
