package com.floriantoenjes.tudo.user;

import lombok.Data;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

@Data
@Relation(collectionRelation = "users")
public class UserResource extends ResourceSupport {

    private String username;

}
