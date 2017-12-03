package com.floriantoenjes.tudo.contactrequest;

import com.floriantoenjes.tudo.user.UserUtils;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

@Component
@RepositoryEventHandler(ContactRequest.class)
public class ContactRequestEventHandler {

    private UserUtils userUtils;

    public ContactRequestEventHandler(UserUtils userUtils) {
        this.userUtils = userUtils;
    }

    @HandleBeforeCreate
    public void addCreatorBasedOnLoggedInUser(ContactRequest contactRequest) {
        contactRequest.setSender(userUtils.getUser());
    }
}
