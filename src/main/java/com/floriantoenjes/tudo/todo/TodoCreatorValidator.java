package com.floriantoenjes.tudo.todo;

import com.floriantoenjes.tudo.user.User;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class TodoCreatorValidator implements ConstraintValidator<ValidateAssignedUsers, Object> {

    @Override
    public void initialize(ValidateAssignedUsers constraintAnnotation) {

    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        Todo todo = (Todo) value;
        List<User> assignedUsers = todo.getAssignedUsers();
        List<User> contacts = todo.getCreator().getContacts();

        if (assignedUsers == null) {
            return true;
        } else if (assignedUsers.stream()
                .anyMatch(assignedUser -> assignedUser.equals(todo.getCreator()))) {
            return false;
        } else if (contacts != null && assignedUsers.stream()
                .noneMatch(assignedUser -> todo.getCreator().getContacts().contains(assignedUser))) {
            return false;
        }

        return true;
    }
}
