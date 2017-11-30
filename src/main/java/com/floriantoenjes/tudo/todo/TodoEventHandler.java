package com.floriantoenjes.tudo.todo;

import com.floriantoenjes.tudo.user.UserUtils;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

@Component
@RepositoryEventHandler(Todo.class)
public class TodoEventHandler {

    private UserUtils userUtils;

    public TodoEventHandler(UserUtils userUtils) {
        this.userUtils = userUtils;
    }

    @HandleBeforeCreate
    public void addCreatorBasedOnLoggedInUser(Todo todo) {
        todo.setCreator(userUtils.getUser());
    }

}
