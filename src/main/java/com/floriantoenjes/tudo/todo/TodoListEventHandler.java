package com.floriantoenjes.tudo.todo;

import com.floriantoenjes.tudo.user.UserUtils;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

@Component
@RepositoryEventHandler(TodoList.class)
public class TodoListEventHandler {

    private UserUtils userUtils;

    public TodoListEventHandler(UserUtils userUtils) {
        this.userUtils = userUtils;
    }

    @HandleBeforeCreate
    public void addCreatorBasedOnLoggedInUser(TodoList todoList) {
        todoList.setCreator(userUtils.getUser());
    }

}
