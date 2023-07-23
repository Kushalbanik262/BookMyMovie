package com.cts.userauthservice.repository;

import java.util.Optional;


import com.cts.userauthservice.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
	boolean existsByEmail(String email);

	Optional<User> findByEmail(String email);
}
