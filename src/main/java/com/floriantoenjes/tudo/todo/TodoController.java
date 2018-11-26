package com.floriantoenjes.tudo.todo;

import com.floriantoenjes.tudo.todo.todoform.TodoForm;
import com.floriantoenjes.tudo.user.User;
import com.floriantoenjes.tudo.user.UserUtils;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

@RepositoryRestController
@BasePathAwareController
public class TodoController {

    private TodoRepository todoRepository;

    private UserUtils userUtils;

    public TodoController(TodoRepository todoRepository, UserUtils userUtils) {
        this.todoRepository = todoRepository;
        this.userUtils = userUtils;
    }

    @PatchMapping("/todo-form/{todoId}")
    @Transactional
    public ResponseEntity<Todo> saveTodoForm(@Valid @RequestBody TodoForm todoForm, @PathVariable Long todoId) {
        Todo todo = this.todoRepository.findOne(todoId);
        if (todo != null) {
            if (isUserAuthorized(todo)) {
                todo.setProgress(todoForm.getProgress());
                todo.setCompleted(todoForm.isCompleted());
                todo.setCompletedAt(todoForm.getCompletedAt());

                Todo savedTodo = this.todoRepository.save(todo);

                return ResponseEntity.ok(savedTodo);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }
        return ResponseEntity.notFound().build();
    }

    private boolean isUserAuthorized(Todo todo) {
        User user = userUtils.getUser();

        return todo.getCreator().equals(user)
                || todo.isAssignedToUser(user.getUsername())
                || userUtils.getUser().getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_ADMIN"));
    }

}
