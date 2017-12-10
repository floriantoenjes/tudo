package com.floriantoenjes.tudo.contactrequest;

import com.floriantoenjes.tudo.user.User;
import org.springframework.data.rest.core.config.Projection;

import java.util.Date;

@Projection(name = "contactRequestProjection", types = ContactRequest.class)
public interface ContactRequestProjection {

    Date getSendAt();

    User getSender();

    User getReceiver();

}
