package com.cts.userauthservice.repository;



import com.cts.userauthservice.model.SecretQuestion;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SecretQuestionRepository extends MongoRepository<SecretQuestion, Long> {

}
