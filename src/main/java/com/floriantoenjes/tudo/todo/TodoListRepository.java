package com.floriantoenjes.tudo.todo;

import com.floriantoenjes.tudo.user.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;

@PreAuthorize("hasRole('ROLE_ADMIN')")
public interface TodoListRepository extends CrudRepository<TodoList, Long> {

    @Override
    @PreAuthorize("permitAll()")
    @PostAuthorize("returnObject != null && returnObject.creator.username == authentication.name")
    TodoList findOne(Long aLong);

    @Override
    Iterable<TodoList> findAll();

    @Override
    Iterable<TodoList> findAll(Iterable<Long> longs);

    @Override
    void delete(Long aLong);

    @Override
    @PreAuthorize("(#entity != null && #entity.creator.username == authentication.name) || hasRole('ROLE_ADMIN')")
    void delete(@Param("entity") TodoList entity);

    @Override
    void delete(Iterable<? extends TodoList> entities);

    @Override
    void deleteAll();

    @PreAuthorize("(#creator != null && #creator.username == authentication.name) || hasRole('ROLE_ADMIN')")
    Iterable<TodoList> findAllByCreator(@Param("creator") User creator);

}
