package com.floriantoenjes.tudo.user;

import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@RepositoryRestController
@BasePathAwareController
public class UserController  {

    private UserRepository repository;

    private UserResourceAssembler assembler;

    public UserController(UserRepository repository, UserResourceAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserResource>> findAll() {
        return ResponseEntity.ok(assembler.toResources(repository.findAll()));
    }

    @GetMapping("/users/{id}")
    public  ResponseEntity<UserResource> findOne(@PathVariable Long id) {
        return ResponseEntity.ok(assembler.toResource(repository.findOne(id)));
    }

}
