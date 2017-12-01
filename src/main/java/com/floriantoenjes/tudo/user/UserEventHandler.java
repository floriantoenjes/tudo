package com.floriantoenjes.tudo.user;

import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

@Component
@RepositoryEventHandler(User.class)
public class UserEventHandler {

    private RoleRepository roleRepository;

    public UserEventHandler(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @HandleBeforeCreate
    public void addCreatorBasedOnLoggedInUser(User user) {
        user.addRole(roleRepository.findByName("ROLE_USER"));
    }

}
