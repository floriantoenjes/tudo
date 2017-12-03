package com.floriantoenjes.tudo.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.floriantoenjes.tudo.contactrequest.ContactRequest;
import com.floriantoenjes.tudo.todo.Todo;
import com.floriantoenjes.tudo.todo.TodoList;
import lombok.Data;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.Configuration;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@ToString(exclude = {"todos", "todoLists", "assignedTodos", "contactRequestsSent", "contactRequestsReceived", "contacts"})
@Entity
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(unique = true)
    private String username;

    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(unique = true)
    private String email;

    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @NotNull
    @JsonIgnore
    private List<Role> roles;

    @OneToMany(mappedBy = "creator")
    private List<TodoList> todoLists;

    @OneToMany(mappedBy = "creator")
    private List<Todo> todos;

    @ManyToMany(mappedBy = "assignedUsers")
    private List<Todo> assignedTodos;

    @ManyToMany
    private List<User> contacts;

    @OneToMany(mappedBy = "sender")
    private List<ContactRequest> contactRequestsSent;

    @OneToMany(mappedBy = "receiver")
    private List<ContactRequest> contactRequestsReceived;

    public User() {
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }

    public boolean addRole(Role role) {
        if (roles == null) {
            roles = new ArrayList<>();
        }

        return roles.add(role);
    }

    public boolean addAssignedTodo(Todo todo) {
        if (assignedTodos == null) {
            assignedTodos = new ArrayList<>();
        }
        return assignedTodos.add(todo);
    }

    public boolean addContact(User contact) {
        if (contacts == null) {
            contacts = new ArrayList<>();
        }
        return contacts.add(contact);
    }

    public boolean addContactRequestSent(ContactRequest contactRequest) {
        if (contactRequestsSent == null) {
            contactRequestsSent = new ArrayList<>();
        }
        return contactRequestsSent.add(contactRequest);
    }

    public boolean addContactRequestReceived(ContactRequest contactRequest) {
        if (contactRequestsReceived == null) {
            contactRequestsReceived = new ArrayList<>();
        }
        return contactRequestsReceived.add(contactRequest);
    }

}
