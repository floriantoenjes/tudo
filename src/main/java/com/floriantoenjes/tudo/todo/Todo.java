package com.floriantoenjes.tudo.todo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.floriantoenjes.tudo.todo.location.Location;
import com.floriantoenjes.tudo.user.User;
import lombok.Data;
import lombok.NonNull;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private String name;

    private String description;

    private Long progress;

    @NotNull
    private boolean completed;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dueDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdated;

    @Temporal(TemporalType.TIMESTAMP)
    private Date completedAt;

    @ElementCollection(targetClass = String.class)
    private List<String> tags;

    @OneToOne(cascade = CascadeType.ALL)
    private Location location;

    @ManyToOne
    private TodoList todoList;

    @ManyToOne
    @NotNull
    @JsonIgnore
    private User creator;

    public Todo() {
    }

    public Todo(String name) {
        this.name = name;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        lastUpdated = new Date();
    }

    public boolean addTag(String tag) {
        if (tags == null) {
            tags = new ArrayList<>();
        }
        return tags.add(tag);
    }
}
