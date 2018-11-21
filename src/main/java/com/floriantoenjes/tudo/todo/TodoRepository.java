package com.floriantoenjes.tudo.todo;

import com.floriantoenjes.tudo.user.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;

@PreAuthorize("hasRole('ROLE_ADMIN')")
public interface TodoRepository extends CrudRepository<Todo, Long> {

    @Override
    @PreAuthorize("#entity.creator.username == authentication.name || hasRole('ROLE_ADMIN')")
    <S extends Todo> S save(@Param("entity") S entity);

    @Override
    <S extends Todo> Iterable<S> save(Iterable<S> entities);

    @Override
    @PreAuthorize("permitAll()")
    @PostAuthorize("returnObject != null && returnObject.creator.username == authentication.name " +
            "|| returnObject != null && returnObject.isAssignedToUser(authentication.name)")
    Todo findOne(Long aLong);

    @Override
    @PreAuthorize("(#entity != null && #entity.creator.username == authentication.name) || hasRole('ROLE_ADMIN')")
    void delete(@Param("entity") Todo entity);

    @PreAuthorize("(#creator != null && #creator.username == authentication.name) || hasRole('ROLE_ADMIN')")
    Iterable<Todo> findAllByCreator(@Param("creator") User creator);


    @PreAuthorize("(#creator != null && #creator.username == authentication.name) || hasRole('ROLE_ADMIN')")
    Iterable<Todo> findAllByCreatorAndTags(@Param("creator") User creator, @Param("tag") String tag);

    @PreAuthorize("(#assignee.username == authentication.name) || hasRole('ROLE_ADMIN')")
    Iterable<Todo> findAllByAssignedUsersContaining(@Param("assignee") User assignee);

}