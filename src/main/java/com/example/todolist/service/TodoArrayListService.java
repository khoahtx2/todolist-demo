package com.example.todolist.service;

import com.example.todolist.dto.TodoRequest;
import com.example.todolist.exception.TodoNotFoundException;
import com.example.todolist.model.Todo;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;


// CRUD
// C: Create
// R: Read
// U: Update
// D: Delete

@Service
public class TodoArrayListService {
    private final List<Todo> store = new ArrayList<>();
    private final AtomicLong sequence = new AtomicLong(1);

    public List<Todo> findAll() {
        // SELECT * FROM todos
        return new ArrayList<>(store);
    }

    public Todo findById(Long id) {
        return store.stream()
                .filter(t -> t.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new TodoNotFoundException(id));
    }

    public Todo create(TodoRequest req) {
        // repository : INSERT INTO todos ().....
        Todo todo = new Todo(
                sequence.getAndIncrement(),
                req.getTitle(),
                req.getDescription(),
                req.getDueDate(),
                req.isCompleted());
        store.add(todo);
        return todo;
    }

    public Todo update(Long id, TodoRequest req) {
        // repository: UPDATE...
        Todo todo = findById(id);
        todo.setTitle(req.getTitle());
        todo.setDescription(req.getDescription());
        todo.setDueDate(req.getDueDate());
        todo.setCompleted(req.isCompleted());
        return todo;
    }

    public void deleteById(Long id) {
        // repository: DELETE...
        Todo todo = findById(id);
        store.remove(todo);
    }
}
