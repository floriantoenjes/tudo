package com.floriantoenjes.tudo.user;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);

}
