package com.floriantoenjes.tudo.user;

import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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
    public ResponseEntity<Resources<Resource<UserResource>>> findAll() {
        Resources<Resource<UserResource>> users = Resources.wrap(assembler.toResources(repository.findAll()));
        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserResource> findOne(@PathVariable Long id) {
        return ResponseEntity.ok(assembler.toResource(repository.findOne(id)));
    }

    @GetMapping("/users/{id}/contacts")
    public ResponseEntity<Resources<Resource<UserResource>>> findAllContacts(@PathVariable Long id) {
        User user = repository.findOne(id);
        if (user != null) {
            Resources<Resource<UserResource>> users = Resources.wrap(assembler.toResources(user.getContacts()));
            return ResponseEntity.ok(users);
        }

        return ResponseEntity.notFound().build();
    }

}
