package com.cts.ticketbookingservice.repository;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.cts.ticketbookingservice.model.User;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

}
