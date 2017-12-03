package com.floriantoenjes.tudo.core;

import com.floriantoenjes.tudo.contactrequest.ContactRequestSentValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.event.ValidatingRepositoryEventListener;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;
import org.springframework.validation.Validator;

import javax.annotation.Resource;


@Configuration
public class RestConfig extends RepositoryRestConfigurerAdapter {

    @Resource(name = "defaultValidator")
    private Validator validator;

    private ContactRequestSentValidator contactRequestSentValidator;

    public RestConfig(ContactRequestSentValidator contactRequestSentValidator) {
        this.contactRequestSentValidator = contactRequestSentValidator;
    }

    @Override
    public void configureValidatingRepositoryEventListener(ValidatingRepositoryEventListener validatingListener) {
        validatingListener.addValidator("beforeCreate", validator);
        validatingListener.addValidator("beforeSave", validator);

        validatingListener.addValidator("beforeCreate", contactRequestSentValidator);
        validatingListener.addValidator("beforeSave", contactRequestSentValidator);
    }
}
