package com.floriantoenjes.tudo.todo;

import com.floriantoenjes.tudo.user.User;
import org.springframework.data.repository.CrudRepository;

public interface TodoRepository extends CrudRepository<Todo, Long> {

    @Override
    Iterable<Todo> findAll();

    Iterable<Todo> findAllByCreator(User creator);
}