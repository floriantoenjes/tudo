package com.floriantoenjes.tudo.core;

import com.floriantoenjes.tudo.contactrequest.ContactRequestSentValidator;
import com.floriantoenjes.tudo.todo.TodoAssignmentValidator;
import com.floriantoenjes.tudo.todo.todoform.TodoForm;
import com.floriantoenjes.tudo.user.AddContactValidator;
import com.floriantoenjes.tudo.user.User;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.core.event.ValidatingRepositoryEventListener;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;
import org.springframework.validation.Validator;

import javax.annotation.Resource;


@Configuration
public class RestConfig extends RepositoryRestConfigurerAdapter {

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

        config.exposeIdsFor(TodoForm.class);
        config.exposeIdsFor(User.class);
    }
}
