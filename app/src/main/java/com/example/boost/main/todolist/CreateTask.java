package com.example.boost.main.todolist;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.boost.R;

import java.util.Calendar;

public class CreateTask extends DialogFragment {

    private EditText titleEditText, descriptionEditText, dateEditText, timeEditText;
    private Button saveButton, cancelButton;
    private Spinner spinnerCategory;
    private Calendar selectedDateTime = Calendar.getInstance();

    public interface OnTaskCreatedListener {
        void onTaskCreated(Task newTask);
    }

    private OnTaskCreatedListener taskCreatedListener;

    public void setTaskCreatedListener(OnTaskCreatedListener listener) {
        this.taskCreatedListener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_task_creation, container, false);

        // Initialize views
        titleEditText = view.findViewById(R.id.titleEditText);
        descriptionEditText = view.findViewById(R.id.descriptionEditText);
        dateEditText = view.findViewById(R.id.dateEditText);
        timeEditText = view.findViewById(R.id.editTextDueTime);
        saveButton = view.findViewById(R.id.saveButton);
        cancelButton = view.findViewById(R.id.cancelButton);
        spinnerCategory = view.findViewById(R.id.spinnerCategory);

        // Date and Time pickers
        dateEditText.setOnClickListener(v -> showDatePicker());
        timeEditText.setOnClickListener(v -> showTimePicker());

        // Save and Cancel button functionality
        saveButton.setOnClickListener(v -> saveTask());
        cancelButton.setOnClickListener(v -> dismiss());

        return view;
    }

    private void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (view, year, month, dayOfMonth) -> {
                    selectedDateTime.set(year, month, dayOfMonth);
                    String formattedDate = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year);
                    dateEditText.setText(formattedDate);
                },
                selectedDateTime.get(Calendar.YEAR),
                selectedDateTime.get(Calendar.MONTH),
                selectedDateTime.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void showTimePicker() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                requireContext(),
                (view, hourOfDay, minute) -> {
                    selectedDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    selectedDateTime.set(Calendar.MINUTE, minute);
                    String formattedTime = String.format("%02d:%02d", hourOfDay, minute);
                    timeEditText.setText(formattedTime);
                },
                selectedDateTime.get(Calendar.HOUR_OF_DAY),
                selectedDateTime.get(Calendar.MINUTE),
                true
        );
        timePickerDialog.show();
    }

    private void saveTask() {
        String title = titleEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();
        String dueDate = dateEditText.getText().toString().trim();
        String dueTime = timeEditText.getText().toString().trim();
        String category = spinnerCategory.getSelectedItem().toString();

        if (title.isEmpty()) {
            Toast.makeText(getContext(), "Title cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        Task newTask = new Task(title, description, dueDate, dueTime, category);
        if (taskCreatedListener != null) {
            taskCreatedListener.onTaskCreated(newTask);
        }

        dismiss();
    }
}
