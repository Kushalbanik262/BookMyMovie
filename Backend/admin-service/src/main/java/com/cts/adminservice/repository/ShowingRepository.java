package com.cts.adminservice.repository;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.cts.adminservice.model.Showing;

@Repository
public interface ShowingRepository extends MongoRepository<Showing, String> {

}
