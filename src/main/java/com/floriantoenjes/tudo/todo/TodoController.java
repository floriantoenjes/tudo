package com.floriantoenjes.tudo.todo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class TodoController {

    private TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping("/todos")
    public Iterable<Todo> getTodos() {
        return todoService.findAll();
    }

    @PostMapping("/todo")
    public Todo saveTodo(@Valid @RequestBody Todo todo) {
        return todoService.save(todo);
    }
}
