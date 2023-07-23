package com.cts.adminservice.repository;



import com.cts.adminservice.model.Theater;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TheaterRepository extends MongoRepository<Theater, String> {

}
