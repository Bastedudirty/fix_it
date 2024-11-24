package com.example.boost.main.todolist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.boost.R;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private List<Task> allTasks = new ArrayList<>();
    private List<Task> filteredTasks = new ArrayList<>();

    public void setTasks(List<Task> tasks) {
        this.allTasks = tasks;
        this.filteredTasks = new ArrayList<>(tasks);
        notifyDataSetChanged();
    }

    public void filterByCategory(String category) {
        if (category.equals("All")) {
            filteredTasks = new ArrayList<>(allTasks);
        } else {
            filteredTasks = allTasks.stream()
                    .filter(task -> task.getCategory().equals(category))
                    .collect(Collectors.toList());
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task_todo, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = filteredTasks.get(position);
        holder.titleTextView.setText(task.getTitle());
        holder.descriptionTextView.setText(task.getDescription());
        holder.categoryTextView.setText(task.getCategory());
        holder.dueDateTextView.setText(task.getDueDate());
        holder.dueTimeTextView.setText(task.getDueTime());
    }

    @Override
    public int getItemCount() {
        return filteredTasks.size();
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, categoryTextView, dueDateTextView, dueTimeTextView, descriptionTextView;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.taskTitle);
            descriptionTextView = itemView.findViewById(R.id.taskDescription);
            categoryTextView = itemView.findViewById(R.id.taskCategory);
            dueDateTextView = itemView.findViewById(R.id.taskDueDate);
            dueTimeTextView = itemView.findViewById(R.id.duetime);
        }
    }
}
