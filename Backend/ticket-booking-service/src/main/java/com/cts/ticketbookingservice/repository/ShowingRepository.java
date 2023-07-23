package com.cts.ticketbookingservice.repository;


import com.cts.ticketbookingservice.model.TicketBooking;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.cts.ticketbookingservice.model.Showing;

import java.util.List;

@Repository
public interface ShowingRepository extends MongoRepository<Showing, String> {


}
