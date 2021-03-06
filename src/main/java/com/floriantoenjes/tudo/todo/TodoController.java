package com.floriantoenjes.tudo.todo;

import com.floriantoenjes.tudo.todo.todoform.TodoForm;
import com.floriantoenjes.tudo.user.User;
import com.floriantoenjes.tudo.user.UserUtils;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.data.rest.webmvc.RepositoryLinksResource;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.Optional;

@RepositoryRestController
@BasePathAwareController
public class TodoController implements ResourceProcessor<RepositoryLinksResource> {

    private TodoRepository todoRepository;

    private UserUtils userUtils;

    public TodoController(TodoRepository todoRepository, UserUtils userUtils) {
        this.todoRepository = todoRepository;
        this.userUtils = userUtils;
    }

    @PatchMapping("/todo-form/{todoId}")
    @Transactional
    public ResponseEntity<Todo> saveTodoForm(@Valid @RequestBody TodoForm todoForm, @PathVariable Long todoId) {
        Optional<Todo> optTodo = this.todoRepository.findById(todoId);
        if (optTodo.isPresent()) {
            Todo todo = optTodo.get();
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

    @Override
    public RepositoryLinksResource process(RepositoryLinksResource resource) {
        resource.add(ControllerLinkBuilder.linkTo(TodoController.class).slash("todo-form")
                .slash("todoId").withRel("todoForm"));
        return resource;
    }
}
