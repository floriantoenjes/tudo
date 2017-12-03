package com.floriantoenjes.tudo.todo;

import com.floriantoenjes.tudo.user.User;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;

@Component
public class TodoAssignmentValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Todo.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Todo todo = (Todo) target;
        List<User> assignedUsers = todo.getAssignedUsers();
        List<User> contacts = todo.getCreator().getContacts();

        if (assignedUsers != null && assignedUsers.size() > 0) {
            if (assignedUsers.stream()
                    .anyMatch(assignedUser -> assignedUser.equals(todo.getCreator()))){
                errors.reject("creatorAssigned", "Todo cannot be assigned to creator.");
            } else if (contacts != null && contacts.size() > 0 && assignedUsers.stream()
                    .noneMatch(contacts::contains)) {
                errors.reject("notAssignedToContact", "Todo must be assigned to a contact of creator.");
            }
        }
    }
}
