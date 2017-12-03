package com.floriantoenjes.tudo.contactrequest;

import com.floriantoenjes.tudo.user.User;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"sender_id", "receiver_id"}))
@Entity
public class ContactRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date sendAt;

    @NotNull
    @ManyToOne
    private User sender;

    @NotNull
    @ManyToOne
    private User receiver;

    @PrePersist
    public void onCreate() {
        sendAt = new Date();
    }
}
