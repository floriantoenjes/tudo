package com.floriantoenjes.tudo.todo;

import com.floriantoenjes.tudo.user.User;
import com.floriantoenjes.tudo.user.UserRepository;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RepositoryEventHandler(Todo.class)
public class TodoEventHandler {

    private TodoRepository todoRepository;

    private UserRepository userRepository;

    public TodoEventHandler(TodoRepository todoRepository, UserRepository userRepository) {
        this.todoRepository = todoRepository;
        this.userRepository = userRepository;
    }

    @HandleBeforeCreate
    public void addCreatorBasedOnLoggedInUser(Todo todo) {
        todo.setCreator(getUser());
    }

    private User getUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username);
    }
}
