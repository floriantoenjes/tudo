package com.floriantoenjes.tudo.contactrequest;

import com.floriantoenjes.tudo.user.User;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
public class ContactRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Date sendAt;

    private User sender;

    private User receiver;
}
