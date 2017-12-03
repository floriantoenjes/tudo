package com.floriantoenjes.tudo.contactrequest;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface ContactRequestRepository extends CrudRepository<ContactRequest, Long> {

    @Override
    @PreAuthorize("!#entity.receiver.username.equals(authentication.name)")
    <S extends ContactRequest> S save(@Param("entity") S entity);

    List<ContactRequest> findAllBySenderIdAndReceiverId(Long senderId, Long receiverId);
}
