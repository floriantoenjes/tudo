package com.floriantoenjes.tudo.todo;

import com.floriantoenjes.tudo.user.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;

public interface TodoRepository extends CrudRepository<Todo, Long> {

//    ToDo: Change the access for admins

    @Override
    @PreAuthorize("#entity.creator.username == authentication.name || hasRole('ROLE_ADMIN')")
    <S extends Todo> S save(@Param("entity") S entity);

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    <S extends Todo> Iterable<S> save(Iterable<S> entities);

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    Iterable<Todo> findAll();

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    Iterable<Todo> findAll(Iterable<Long> longs);

    @Override
    @PostAuthorize("returnObject != null && returnObject.creator.username == authentication.name " +
            "|| returnObject.isAssignedToUser(authentication.name)")
    Todo findOne(Long aLong);

    @Override
    @PreAuthorize("#entity != null && #entity.creator.username == authentication.name")
    void delete(@Param("entity") Todo entity);

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    void delete(Long aLong);

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    void delete(Iterable<? extends Todo> entities);

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    void deleteAll();

    @PreAuthorize("#creator != null && #creator.username == authentication.name")
    Iterable<Todo> findAllByCreator(@Param("creator") User creator);


    @PreAuthorize("#creator != null && #creator.username == authentication.name")
    Iterable<Todo> findAllByCreatorAndTags(@Param("creator") User creator, @Param("tag") String tag);

    @PreAuthorize("#assignee.username == authentication.name")
    Iterable<Todo> findAllByAssignedUsersContaining(@Param("assignee") User assignee);

}