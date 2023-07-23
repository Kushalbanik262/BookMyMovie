package com.cts.ticketbookingservice.repository;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.cts.ticketbookingservice.model.TicketBooking;

import java.util.List;

@Repository
public interface TicketBookingRepository extends MongoRepository<TicketBooking, String> {
    @Query("{'user._id' : ?0 }")
    public List<TicketBooking> findBookingsByUserId(String userId);
	
}
