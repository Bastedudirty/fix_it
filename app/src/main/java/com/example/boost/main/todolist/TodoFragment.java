package com.example.boost.main.todolist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.boost.R;

import java.util.ArrayList;
import java.util.List;

public class TodoFragment extends Fragment {

    private RecyclerView recyclerView;
    private TaskAdapter taskAdapter;
    private Spinner categorySpinner;
    private Button createTaskButton;
    private List<Task> allTasks = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todo, container, false);

        recyclerView = view.findViewById(R.id.list_item);
        categorySpinner = view.findViewById(R.id.categorySpinner);
        createTaskButton = view.findViewById(R.id.fabAddTask);

        taskAdapter = new TaskAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(taskAdapter);

        setupCategorySpinner();
        createTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CreateTask();
            }
        });
        return view;
    }

    private void setupCategorySpinner() {

        List<String> categories = new ArrayList<>();
        categories.add("All");
        categories.add("Personal");
        categories.add("Study");
        categories.add("Shopping");
        categories.add("Others");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCategory = categories.get(position);
                filterTasks(selectedCategory);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                filterTasks("All");
            }
        });
    }


    private void filterTasks(String category) {
        if (category.equals("All")) {
            taskAdapter.setTasks(new ArrayList<>(allTasks));
        } else {
            List<Task> filteredTasks = new ArrayList<>();
            for (Task task : allTasks) {
                if (task.getCategory().equals(category)) {
                    filteredTasks.add(task);
                }
            }
            taskAdapter.setTasks(filteredTasks);
        }
    }
}
