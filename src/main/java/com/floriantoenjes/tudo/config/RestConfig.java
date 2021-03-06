package com.floriantoenjes.tudo.config;

import com.floriantoenjes.tudo.contactrequest.ContactRequestSentValidator;
import com.floriantoenjes.tudo.todo.TodoAssignmentValidator;
import com.floriantoenjes.tudo.todo.todoform.TodoForm;
import com.floriantoenjes.tudo.user.AddContactValidator;
import com.floriantoenjes.tudo.user.User;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.core.event.ValidatingRepositoryEventListener;
import org.springframework.data.rest.core.mapping.ExposureConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.validation.Validator;

import javax.annotation.Resource;


@Configuration
public class RestConfig implements RepositoryRestConfigurer {

    @Resource(name = "defaultValidator")
    private Validator validator;

    private AddContactValidator addContactValidator;

    private ContactRequestSentValidator contactRequestSentValidator;

    private TodoAssignmentValidator todoAssignmentValidator;

    public RestConfig(AddContactValidator addContactValidator, ContactRequestSentValidator contactRequestSentValidator, TodoAssignmentValidator todoAssignmentValidator) {
        this.addContactValidator = addContactValidator;
        this.contactRequestSentValidator = contactRequestSentValidator;
        this.todoAssignmentValidator = todoAssignmentValidator;
    }

    @Override
    public void configureValidatingRepositoryEventListener(ValidatingRepositoryEventListener validatingListener) {
        validatingListener.addValidator("beforeCreate", validator);
        validatingListener.addValidator("beforeSave", validator);

        validatingListener.addValidator("beforeCreate", contactRequestSentValidator);
        validatingListener.addValidator("beforeSave", contactRequestSentValidator);

        validatingListener.addValidator("beforeCreate", todoAssignmentValidator);
        validatingListener.addValidator("beforeSave", todoAssignmentValidator);
        validatingListener.addValidator("beforeLinkSave", todoAssignmentValidator);

        validatingListener.addValidator("beforeLinkSave", addContactValidator);
    }

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        config.getCorsRegistry()
                .addMapping("/**")
                .allowedMethods("GET", "POST", "PUT", "DELETE");

        ExposureConfiguration exposureConfiguration = config.getExposureConfiguration();
        exposureConfiguration.forDomainType(User.class)
                .withAssociationExposure((metdata, httpMethods) -> httpMethods.disable(HttpMethod.GET));
        exposureConfiguration.forDomainType(User.class)
                .withItemExposure((metdata, httpMethods) -> httpMethods.disable(HttpMethod.PATCH, HttpMethod.PUT));
    }
}
