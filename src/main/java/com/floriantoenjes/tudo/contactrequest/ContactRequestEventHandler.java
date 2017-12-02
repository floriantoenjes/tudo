package com.floriantoenjes.tudo.contactrequest;

import com.floriantoenjes.tudo.user.UserUtils;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

@Component
@RepositoryEventHandler
public class ContactRequestEventHandler {

    private UserUtils userUtils;

    @HandleBeforeCreate
    public void addCreatorBasedOnLoggedInUser(ContactRequest contactRequest) {
        contactRequest.setSender(userUtils.getUser());
    }
}
