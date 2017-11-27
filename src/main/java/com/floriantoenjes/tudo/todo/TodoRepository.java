package com.floriantoenjes.tudo.todo;

import com.floriantoenjes.tudo.user.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PostAuthorize;

public interface TodoRepository extends CrudRepository<Todo, Long> {

    @Override
    Iterable<Todo> findAll();

    @PostAuthorize("returnObject!=null?returnObject.iterator().hasNext()==true&&returnObject.iterator().next().creator.username==principal.username:true")
    Iterable<Todo> findAllByCreator(@Param("creator") User creator);
}