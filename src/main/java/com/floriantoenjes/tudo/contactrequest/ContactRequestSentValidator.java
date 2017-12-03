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

        if (contactRequestRepository.findBySenderIdAndReceiverId(contactRequest.getSender().getId(),
                        contactRequest.getReceiver().getId()) != null

                || contactRequestRepository.findBySenderIdAndReceiverId(contactRequest.getReceiver().getId(),
                contactRequest.getSender().getId())  != null) {

            errors.reject("exists", "Contact request has already been sent.");
        }
    }

}
