package com.floriantoenjes.tudo.user;

import com.floriantoenjes.tudo.contactrequest.ContactRequest;
import com.floriantoenjes.tudo.contactrequest.ContactRequestRepository;
import com.floriantoenjes.tudo.util.NoContactRequestException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class AddContactValidator implements Validator {

    private ContactRequestRepository contactRequestRepository;

    private UserRepository userRepository;

    public AddContactValidator(ContactRequestRepository contactRequestRepository, UserRepository userRepository) {
        this.contactRequestRepository = contactRequestRepository;
        this.userRepository = userRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;

        if (user.getContacts().size() == 0) {
            return;
        }

        for (User contact : user.getContacts()) {
            ContactRequest toUser = contactRequestRepository.findBySenderUsernameAndReceiverUsername(contact.getUsername(), user.getUsername());
            if (toUser != null) {
                addContactAndSave(user, contact);
                user.getContactRequestsReceived().remove(toUser);
                contact.getContactRequestsReceived().remove(toUser);
                contactRequestRepository.delete(toUser);
                return;
            }
        }
        errors.reject("noContactRequest", "Contact request has to be present to add a contact.");
    }

    private void addContactAndSave(User user, User contact) {
        try {
            user.addContact(contact);
            userRepository.save(user);
        } catch (NoContactRequestException e) {
            e.printStackTrace();
        }
    }
}
