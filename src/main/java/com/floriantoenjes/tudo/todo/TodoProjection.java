package com.floriantoenjes.tudo.todo;

import com.floriantoenjes.tudo.todo.location.Location;
import com.floriantoenjes.tudo.todo.todoform.TodoForm;
import org.springframework.data.rest.core.config.Projection;

import java.util.Date;
import java.util.List;

@Projection(name = "todoProjection", types = Todo.class)
public interface TodoProjection {

    String getName();

    String getDescription();

    Date getCreatedAt();

    Date getDueDate();

    List<String> getTags();

    Location getLocation();

    TodoForm getTodoForm();

}
