package com.floriantoenjes.tudo.todo;

import com.floriantoenjes.tudo.category.Category;
import com.floriantoenjes.tudo.todo.location.Location;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String description;

    private Long progress;

    private boolean completed;

    private Date createdAt;

    private Date dueDate;

    private Date completedAt;

    @OneToOne
    private Location location;

    @ManyToOne
    private TodoList todoList;

    @ManyToMany(mappedBy = "todos")
    private List<Category> categories;

}
