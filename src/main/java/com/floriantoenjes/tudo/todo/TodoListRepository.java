package com.floriantoenjes.tudo.todo;

import com.floriantoenjes.tudo.user.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface TodoListRepository extends CrudRepository<TodoList, Long> {

    Iterable<TodoList> findAllByCreator(@Param("creator") User creator);

}
