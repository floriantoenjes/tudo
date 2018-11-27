package com.floriantoenjes.tudo.user;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class UserResourceAssembler extends ResourceAssemblerSupport<User, UserResource> {

    @Value("${spring.data.rest.basePath}")
    private String apiBasePath;

    public UserResourceAssembler(){
        super(UserController.class, UserResource.class);
    }

    @Override
    public UserResource toResource(User entity) {
        UserResource userResource = new UserResource();
        userResource.setUsername(entity.getUsername());

        userResource.add(ControllerLinkBuilder.linkTo(UserController.class)
                .slash(apiBasePath).slash(entity).withSelfRel());

        return userResource;
    }

}
