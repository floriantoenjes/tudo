package com.floriantoenjes.tudo.contactrequest;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface ContactRequestRepository extends CrudRepository<ContactRequest, Long> {

    @Override
    @PreAuthorize("!#entity.receiver.username.equals(authentication.name)")
    <S extends ContactRequest> S save(@Param("entity") S entity);

    @PreAuthorize("authentication.name.equals(#receiverName)")
    Iterable<ContactRequest> findAllByReceiverUsername(@Param("receiverName") String receiverName);

    @PreAuthorize("authentication.name.equals(#senderName) || authentication.name.equals(#receiverName)")
    ContactRequest findBySenderUsernameAndReceiverUsername(@Param("senderName") String senderName,
                                                           @Param("receiverName") String receiverName);

    @Override
    @PostAuthorize("returnObject != null && (returnObject.sender.username == authentication.name " +
            "|| returnObject.receiver.username == authentication.name)")
    ContactRequest findOne(@Param("contactRequestId") Long aLong);

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    Iterable<ContactRequest> findAll();

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    Iterable<ContactRequest> findAll(Iterable<Long> longs);

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    void delete(Long aLong);

    @Override
    @PreAuthorize("#entity.sender.username == authentication.name || #entity.receiver.username == authentication.name")
    void delete(@Param("entity") ContactRequest entity);

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    void delete(Iterable<? extends ContactRequest> entities);

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    void deleteAll();



}
