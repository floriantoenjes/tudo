package com.floriantoenjes.tudo.user;

import lombok.Data;
import org.springframework.hateoas.ResourceSupport;

@Data
public class UserResource extends ResourceSupport {

    private String username;

}
