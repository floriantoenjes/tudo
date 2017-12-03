package com.floriantoenjes.tudo.contactrequest;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.method.P;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface ContactRequestRepository extends CrudRepository<ContactRequest, Long> {

    @Override
    @PreAuthorize("!#entity.receiver.username.equals(authentication.name)")
    <S extends ContactRequest> S save(@Param("entity") S entity);

    @PreAuthorize("authentication.principal.id == #senderId || authentication.principal.id == #receiverId")
    List<ContactRequest> findAllBySenderIdAndReceiverId(@Param("senderId") Long senderId, @Param("receiverId") Long receiverId);

    @Override
    @PostAuthorize("returnObject.sender.username == authentication.name " +
            "|| returnObject.receiver.username == authentication.name")
    ContactRequest findOne(@Param("contactRequestId") Long aLong);
}
