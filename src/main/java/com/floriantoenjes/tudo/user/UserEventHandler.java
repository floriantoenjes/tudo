package com.floriantoenjes.tudo.user;

import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeLinkDelete;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RepositoryEventHandler(User.class)
public class UserEventHandler {

    private RoleRepository roleRepository;

    private UserRepository userRepository;

    public UserEventHandler(RoleRepository roleRepository, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    @HandleBeforeCreate
    public void addCreatorBasedOnLoggedInUser(User user) {
        user.addRole(roleRepository.findByName("ROLE_USER"));
    }

    @HandleBeforeLinkDelete
    public void deleteRemainingContact(User user, Set<User> contacts) {
        for (User previousContact : user.getPreviousContacts()) {
            User currentContact = null;
            boolean found = false;

            for (User contact : contacts) {
                currentContact = contact;
                if (previousContact.equals(contact)) {
                    found = true;
                }
            }
            if (currentContact != null && !found) {
                previousContact.removeContact(user);
                userRepository.save(currentContact);
            }
        }
    }

}
