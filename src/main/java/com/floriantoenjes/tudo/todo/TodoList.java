package com.floriantoenjes.tudo.todo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.floriantoenjes.tudo.user.User;
import com.floriantoenjes.tudo.util.UnauthorizedException;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@ToString(exclude = "todos")
@Entity
public class TodoList {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "todoList", cascade = CascadeType.REMOVE)
    private List<Todo> todos;

    @ManyToOne
    @JsonIgnore
    private User creator;

    public TodoList() {
    }

    public TodoList(String name) {
        this.name = name;
    }

    public boolean addTodo(Todo todo) throws UnauthorizedException {
        if (!creator.equals(todo.getCreator())) {
            throw new UnauthorizedException("Todo list author must be the same as todo author");
        }
        if (todos == null) {
            todos = new ArrayList<>();
        }

        todo.setTodoList(this);

        return todos.add(todo);
    }

}
