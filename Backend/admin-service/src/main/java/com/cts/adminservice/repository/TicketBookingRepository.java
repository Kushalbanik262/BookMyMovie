package com.cts.adminservice.repository;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.cts.adminservice.model.TicketBooking;

@Repository
public interface TicketBookingRepository extends MongoRepository<TicketBooking, String> {

}
