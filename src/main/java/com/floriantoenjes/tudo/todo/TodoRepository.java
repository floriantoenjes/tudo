package com.floriantoenjes.tudo.todo;

import com.floriantoenjes.tudo.user.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;

public interface TodoRepository extends CrudRepository<Todo, Long> {

    @Override
    @Secured("ROLE_ADMIN")
    Iterable<Todo> findAll();

    @Override
    @Secured("ROLE_ADMIN")
    Iterable<Todo> findAll(Iterable<Long> longs);

    @Override
    @PostAuthorize("returnObject.creator.username == authentication.name")
    Todo findOne(Long aLong);

    @Override
    @PreAuthorize("#entity.creator.username == authentication.name")
    void delete(@Param("entity") Todo entity);

    @Override
    @Secured("ROLE_ADMIN")
    void delete(Long aLong);

    @Override
    @Secured("ROLE_ADMIN")
    void delete(Iterable<? extends Todo> entities);

    @Override
    @Secured("ROLE_ADMIN")
    void deleteAll();

    @PreAuthorize("#creator != null && #creator.username == authentication.name")
    Iterable<Todo> findAllByCreator(@Param("creator") User creator);


}