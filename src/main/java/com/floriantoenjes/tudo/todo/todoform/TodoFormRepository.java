package com.floriantoenjes.tudo.todo.todoform;

import org.springframework.data.repository.CrudRepository;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;

public interface TodoFormRepository extends CrudRepository<TodoForm, Long> {
    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    <S extends TodoForm> S save(S entity);

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    <S extends TodoForm> Iterable<S> save(Iterable<S> entities);

    @Override
    @PostAuthorize("returnObject != null && returnObject.todo.creator.username == authentication.name " +
            "|| returnObject.todo.isAssignedToUser(authentication.name)")
    TodoForm findOne(Long aLong);

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    Iterable<TodoForm> findAll();

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    Iterable<TodoForm> findAll(Iterable<Long> longs);

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    void delete(Long aLong);

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    void delete(TodoForm entity);

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    void delete(Iterable<? extends TodoForm> entities);

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    void deleteAll();
}
