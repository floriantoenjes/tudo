package com.floriantoenjes.tudo.todo;

import com.floriantoenjes.tudo.user.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Optional;

@PreAuthorize("hasRole('ROLE_ADMIN')")
public interface TodoListRepository extends CrudRepository<TodoList, Long> {

    @Override
    @PreAuthorize("permitAll()")
    @PostAuthorize("returnObject.get() != null && returnObject.get().creator.username == authentication.name || hasRole('ROLE_ADMIN')")
    Optional<TodoList> findById(Long aLong);

    @Override
    @PreAuthorize("(#entity != null && #entity.creator.username == authentication.name) || hasRole('ROLE_ADMIN')")
    void delete(@Param("entity") TodoList entity);

    @PreAuthorize("(#creator != null && #creator.username == authentication.name) || hasRole('ROLE_ADMIN')")
    Iterable<TodoList> findAllByCreator(@Param("creator") User creator);

    @Override
    @PreAuthorize("hasRole('ROLE_USER')")
    <S extends TodoList> S save(S entity);
}
