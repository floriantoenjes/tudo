package com.floriantoenjes.tudo.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.floriantoenjes.tudo.contactrequest.ContactRequest;
import com.floriantoenjes.tudo.todo.Todo;
import com.floriantoenjes.tudo.todo.TodoList;
import com.floriantoenjes.tudo.util.NoContactRequestException;
import lombok.Data;
import lombok.ToString;
import org.springframework.hateoas.Identifiable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Data
@ToString(exclude = {"todos", "todoLists", "assignedTodos",
        "contactRequestsSent", "contactRequestsReceived", "contacts", "previousContacts"})
@Entity
public class User implements Identifiable<Long>, UserDetails {

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
    private Set<Role> roles;

    @OneToMany(mappedBy = "creator")
    private Set<TodoList> todoLists;

    @OneToMany(mappedBy = "creator")
    private Set<Todo> todos;

    @ManyToMany(mappedBy = "assignedUsers")
    private Set<Todo> assignedTodos;

    @ManyToMany
    private Set<User> contacts;

    @OneToMany(mappedBy = "sender")
    private Set<ContactRequest> contactRequestsSent;

    @OneToMany(mappedBy = "receiver")
    private Set<ContactRequest> contactRequestsReceived;

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

    @Transient
    @JsonIgnore
    private Set<User> previousContacts;

    public boolean addRole(Role role) {
        if (roles == null) {
            roles = new HashSet<>();
        }

        return roles.add(role);
    }

    public boolean addAssignedTodo(Todo todo) {
        if (assignedTodos == null) {
            assignedTodos = new HashSet<>();
        }
        return assignedTodos.add(todo);
    }

    public boolean addContact(User contact) throws NoContactRequestException {
        if (contacts == null) {
            contacts = new HashSet<>();
        }

        if (contactRequestsReceived == null && contactRequestsSent == null) {
            throw new NoContactRequestException("No contact request for " + contact.getUsername() + " found.");
        }

        if (contactRequestsReceived != null
                && contactRequestsReceived.stream()
                .noneMatch(contactRequest -> contact.equals(contactRequest.getSender())) &&

                contactRequestsSent != null
                && contactRequestsSent.stream()
                .noneMatch(contactRequest -> contact.equals(contactRequest.getReceiver()))) {

            throw new NoContactRequestException("No contact request for " + contact.getUsername() + " found.");

        }

        return contacts.add(contact);
    }

    public boolean removeContact(User contact) {
        if (contacts == null) {
            return false;
        }
        return contacts.remove(contact);
    }

    public boolean removeContactRequestSent(ContactRequest contactRequest) {
        return this.contactRequestsSent.remove(contactRequest);
    }

    public boolean removeContactRequestReceived(ContactRequest contactRequest) {
        return this.contactRequestsReceived.remove(contactRequest);
    }

    public boolean addContactRequestSent(ContactRequest contactRequest) {
        if (contactRequestsSent == null) {
            contactRequestsSent = new HashSet<>();
        }
        return contactRequestsSent.add(contactRequest);
    }

    public boolean addContactRequestReceived(ContactRequest contactRequest) {
        if (contactRequestsReceived == null) {
            contactRequestsReceived = new HashSet<>();
        }
        return contactRequestsReceived.add(contactRequest);
    }

    @PostLoad
    private void savePreviousContacts() {
        previousContacts = new HashSet<>(contacts);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        User user = (User) o;

        return username.equals(user.username);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + username.hashCode();
        return result;
    }
}
