package com.floriantoenjes.tudo.todo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.floriantoenjes.tudo.todo.location.Location;
import com.floriantoenjes.tudo.todo.todoform.TodoForm;
import com.floriantoenjes.tudo.user.User;
import com.floriantoenjes.tudo.util.NoContactException;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.*;

@Data
@Entity
@ToString(exclude = "todoForm")
@EqualsAndHashCode(exclude = "todoForm")
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private String name;

    private String description;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dueDate;

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

    @ManyToMany
    private Set<User> assignedUsers;

    @OneToOne(mappedBy = "todo", cascade = CascadeType.ALL)
    private TodoForm todoForm;

    public Todo() {
    }

    public Todo(String name) {
        this.name = name;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
        todoForm = new TodoForm();
        todoForm.setTodo(this);
    }

    public boolean addTag(String tag) {
        if (tags == null) {
            tags = new ArrayList<>();
        }
        return tags.add(tag);
    }

    public boolean assignToUser(User assignee) throws NoContactException {
        if (creator.getContacts() == null || !creator.getContacts().contains(assignee)) {
            throw new NoContactException("A todo can only be assigned to contacts of the todo creator.");
        }
        if (assignedUsers == null) {
            assignedUsers = new HashSet<>();
        }
        assignee.addAssignedTodo(this);

        return assignedUsers.add(assignee);
    }

    public boolean isAssignedToUser(String username) {
        return assignedUsers.stream().anyMatch(assignedUser -> assignedUser.getUsername().equals(username));
    }
}
