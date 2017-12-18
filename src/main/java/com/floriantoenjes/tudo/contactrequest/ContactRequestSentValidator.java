package com.floriantoenjes.tudo.contactrequest;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ContactRequestSentValidator implements Validator {

    private ContactRequestRepository contactRequestRepository;

    public ContactRequestSentValidator(ContactRequestRepository contactRequestRepository) {
        this.contactRequestRepository = contactRequestRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return ContactRequest.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ContactRequest contactRequest = (ContactRequest) target;

        if (contactRequest.getSender() == null || contactRequest.getReceiver() == null) {
            return;
        }

        if (contactRequestRepository.findBySenderUsernameAndReceiverUsername(contactRequest.getSender().getUsername(),
                        contactRequest.getReceiver().getUsername()) != null

                || contactRequestRepository.findBySenderUsernameAndReceiverUsername(contactRequest.getReceiver().getUsername(),
                contactRequest.getSender().getUsername())  != null) {

            errors.reject("exists", "Contact request has already been sent.");
        }
    }

}
