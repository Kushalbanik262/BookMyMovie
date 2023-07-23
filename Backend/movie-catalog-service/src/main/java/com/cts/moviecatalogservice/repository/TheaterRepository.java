package com.cts.moviecatalogservice.repository;

import com.cts.moviecatalogservice.model.Theater;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TheaterRepository extends MongoRepository<Theater,String> {
}
