package com.example.todolist.dto;

import com.example.todolist.model.Todo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

public class TodoRequest {
    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title must be at most 100 characters")
    private String title;

    @Size(max = 500, message = "Description must be at most 500 characters")
    private String description;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dueDate;

    private boolean completed;

    public static TodoRequest from(Todo todo) {
        TodoRequest r = new TodoRequest();
        r.setTitle(todo.getTitle());
        r.setDescription(todo.getDescription());
        r.setDueDate(todo.getDueDate());
        r.setCompleted(todo.isCompleted());
        return r;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
