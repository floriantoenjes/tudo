package com.floriantoenjes.tudo.user;

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
        for (User contact : user.getContacts()) {
            if (contactRequestRepository.findAllBySenderIdAndReceiverId(user.getId(), contact.getId()).size() == 0 &&
                    contactRequestRepository.findAllBySenderIdAndReceiverId(contact.getId(), user.getId()).size() == 0) {
                errors.reject("noContactRequest", "Contact request has to be present to add a contact");
            }
        }
    }
}
