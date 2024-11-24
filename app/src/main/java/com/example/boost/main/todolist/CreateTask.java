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

        titleEditText = view.findViewById(R.id.titleEditText);
        descriptionEditText = view.findViewById(R.id.descriptionEditText);
        dateEditText = view.findViewById(R.id.dateEditText);
        timeEditText = view.findViewById(R.id.editTextDueTime);
        saveButton = view.findViewById(R.id.saveButton);
        cancelButton = view.findViewById(R.id.cancelButton);
        spinnerCategory = view.findViewById(R.id.spinnerCategory);

        dateEditText.setOnClickListener(v -> showDatePicker());
        timeEditText.setOnClickListener(v -> showTimePicker());

        saveButton.setOnClickListener(v -> saveTask());
        cancelButton.setOnClickListener(v -> dismiss());

        return view;
    }

    private void showDatePicker() {
        Calendar currentDate = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                (view, year, month, dayOfMonth) -> {
                    selectedDateTime.set(year, month, dayOfMonth);
                    dateEditText.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                },
                currentDate.get(Calendar.YEAR),
                currentDate.get(Calendar.MONTH),
                currentDate.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void showTimePicker() {
        Calendar currentTime = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                getContext(),
                (timePicker, hourOfDay, minute) -> {
                    selectedDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    selectedDateTime.set(Calendar.MINUTE, minute);
                    timeEditText.setText(String.format("%02d:%02d", hourOfDay, minute));
                },
                currentTime.get(Calendar.HOUR_OF_DAY),
                currentTime.get(Calendar.MINUTE),
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

        Task newTask;

        if (dueDate.isEmpty() || dueTime.isEmpty() || category.isEmpty()) {
            newTask = new Task(title, description);
        } else {
            newTask = new Task(title, description, dueDate, dueTime, category);
        }

        if (taskCreatedListener != null) {
            taskCreatedListener.onTaskCreated(newTask);
        }

        dismiss();
    }
}
