package com.floriantoenjes.tudo.todo;

import com.floriantoenjes.tudo.user.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;

public interface TodoRepository extends CrudRepository<Todo, Long> {

    @Override
    Iterable<Todo> findAll();

    @PreAuthorize("#creator.username == authentication.name")
    Iterable<Todo> findAllByCreator(@Param("creator") User creator);
}