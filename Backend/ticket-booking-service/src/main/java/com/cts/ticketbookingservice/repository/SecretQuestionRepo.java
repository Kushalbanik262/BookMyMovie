package com.cts.ticketbookingservice.repository;

import com.cts.ticketbookingservice.model.SecretQuestion;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SecretQuestionRepo extends MongoRepository<SecretQuestion,String> {
}
