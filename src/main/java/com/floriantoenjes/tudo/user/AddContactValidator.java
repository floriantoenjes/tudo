package com.floriantoenjes.tudo.user;

import com.floriantoenjes.tudo.contactrequest.ContactRequest;
import com.floriantoenjes.tudo.contactrequest.ContactRequestRepository;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class AddContactValidator implements Validator {

    private ContactRequestRepository contactRequestRepository;

    public AddContactValidator(ContactRequestRepository contactRequestRepository) {
        this.contactRequestRepository = contactRequestRepository;
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
            ContactRequest fromUser = contactRequestRepository.findBySenderIdAndReceiverId(user.getId(), contact.getId());
            if (fromUser != null) {
                contactRequestRepository.delete(fromUser);
                return;
            }
            ContactRequest toUser = contactRequestRepository.findBySenderIdAndReceiverId(contact.getId(), user.getId());
            if (toUser != null) {
                contactRequestRepository.delete(toUser);
                return;
            }
        }
        errors.reject("noContactRequest", "Contact request has to be present to add a contact.");
    }
}
