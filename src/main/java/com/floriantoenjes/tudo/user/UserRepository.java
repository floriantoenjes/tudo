package com.floriantoenjes.tudo.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Optional;

@PreAuthorize("hasRole('ROLE_ADMIN')")
public interface UserRepository extends PagingAndSortingRepository<User, Long> {

    @PreAuthorize("permitAll()")
    User findByUsername(String username);

    @Override
    @PreAuthorize("permitAll()")
    Iterable<User> findAll(Sort sort);

    @Override
    @PreAuthorize("permitAll()")
    Page<User> findAll(Pageable pageable);

    @Override
    @PreAuthorize("permitAll()")
    Iterable<User> findAll();

    @Override
    @PreAuthorize("permitAll()")
    Iterable<User> findAllById(Iterable<Long> longs);

    @Override
    @PreAuthorize("permitAll()")
    Optional<User> findById(Long aLong);

    @Override
    @PreAuthorize("permitAll()")
//    @RestResource(exported = false)
    <S extends User> S save(@Param("entity") S entity);
}
