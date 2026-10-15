package com.example.todolist.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.todolist.dto.TodoRequest;
import com.example.todolist.exception.TodoNotFoundException;
import com.example.todolist.model.Todo;
import com.example.todolist.repository.TodoRepository;

@Service
@Transactional 
public class TodoService {
    private final EmailService emailService;
    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository, EmailService emailService, CustomUserDetailsService customUserDetailsService) {
        this.todoRepository = todoRepository;
        this.emailService = emailService;
    }

    @Transactional (readOnly = true)
    public List<Todo> findAll() {
        return this.todoRepository.findAll();
    }

    public Todo findById(Long id) {
        return todoRepository.findById(id)
                .orElseThrow(() -> new TodoNotFoundException(id));
    }

    public Todo create(TodoRequest req) {
        Todo todo = new Todo(
                req.getTitle(),
                req.getDescription(),
                req.getDueDate(),
                req.isCompleted());

        Todo savedTodo = todoRepository.save(todo);

        emailService.sendTodoNotificationEmail("userA@localhost.com", savedTodo);

        return savedTodo;
    }

    public Todo update(Long id, TodoRequest req) {
        Todo todo = findById(id);
        todo.setTitle(req.getTitle());
        todo.setDescription(req.getDescription());
        todo.setDueDate(req.getDueDate());
        todo.setCompleted(req.isCompleted());
        return todoRepository.save(todo);
    }

    public void deleteById(Long id) {
        Todo todo = findById(id);
        todoRepository.delete(todo);
    }
}
