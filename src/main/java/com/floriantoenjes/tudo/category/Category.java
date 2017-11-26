package com.floriantoenjes.tudo.category;

import com.floriantoenjes.tudo.todo.Todo;

import javax.persistence.*;
import java.util.List;

@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @ManyToMany
    private List<Todo> todos;
}
