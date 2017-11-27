package com.floriantoenjes.tudo.todo;

import com.floriantoenjes.tudo.user.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

public interface TodoRepository extends CrudRepository<Todo, Long> {

    @Override
    Iterable<Todo> findAll();

    Iterable<Todo> findAllByCreator(@Param("creator") User creator);
}