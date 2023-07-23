package com.cts.moviecatalogservice.repository;

import com.cts.moviecatalogservice.model.Showing;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ShowingRepository extends MongoRepository<Showing,String> {
}
