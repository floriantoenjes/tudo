package com.floriantoenjes.tudo.todo.todoform;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;

@PreAuthorize("hasRole('ROLE_ADMIN')")
public interface TodoFormRepository extends CrudRepository<TodoForm, Long> {

    @Override
    @PreAuthorize("#entity.todo.creator.username == authentication.name || " +
            "#entity.todo.isAssignedToUser(authentication.name) || hasRole('ROLE_ADMIN')")
    <S extends TodoForm> S save(@Param("entity") S entity);

    @Override
    <S extends TodoForm> Iterable<S> save(Iterable<S> entities);

    @Override
    @PreAuthorize("permitAll()")
    @PostAuthorize("returnObject != null && returnObject.todo.creator.username == authentication.name " +
            "|| returnObject.todo.isAssignedToUser(authentication.name)")
    TodoForm findOne(Long aLong);

    @Override
    Iterable<TodoForm> findAll();

    @Override
    Iterable<TodoForm> findAll(Iterable<Long> longs);

    @Override
    void delete(Long aLong);

    @Override
    void delete(TodoForm entity);

    @Override
    void delete(Iterable<? extends TodoForm> entities);

    @Override
    void deleteAll();
}
