package com.example.todolist.controller;

import com.example.todolist.dto.TodoRequest;
import com.example.todolist.exception.TodoNotFoundException;
import com.example.todolist.model.Todo;
import com.example.todolist.service.EmailService;
import com.example.todolist.service.TodoService;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/todos") // http://localhost:8080/todos
public class TodoController {
    private final TodoService todoService;

    public TodoController(TodoService todoService, EmailService emailService) {
        this.todoService = todoService;
    }

    // GET http://localhost:8080/todos
    @GetMapping
    public String list(Model model) {
        List<Todo> todos = todoService.findAll();

        model.addAttribute("todos", todos);

        return "todos/list";
    }

    // GET http://localhost:8080/todos/new
    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("todo", new TodoRequest());
        model.addAttribute("todoId", null);
        return "todos/form"; // resources/templates/todos/form.html
    }

    // POST http://localhost:8080/todos/new
    @PostMapping
    public String create(@ModelAttribute("todo") @Valid TodoRequest request,
            BindingResult validationResult,
            Model model) {
        if (validationResult.hasErrors()) {
            model.addAttribute("todoId", null);
            return "todos/form";
        }
        todoService.create(request);

        return "redirect:/todos";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Todo todo = todoService.findById(id);
        TodoRequest request = TodoRequest.from(todo);

        model.addAttribute("todo", request);
        model.addAttribute("todoId", id);

        return "todos/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
            @Valid @ModelAttribute("todo") TodoRequest request,
            BindingResult result,
            Model model) {
        if (result.hasErrors()) {
            model.addAttribute("todoId", id);
            return "todos/form";
        }
        todoService.update(id, request);
        return "redirect:/todos";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        todoService.deleteById(id);
        return "redirect:/todos";
    }

    @ExceptionHandler(TodoNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(TodoNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                        "error", "NOT_FOUND",
                        "message", ex.getMessage()));
    }
}
