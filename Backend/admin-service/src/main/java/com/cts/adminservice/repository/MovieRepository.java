package com.cts.adminservice.repository;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.cts.adminservice.model.Movie;

@Repository
public interface MovieRepository extends MongoRepository<Movie, String> {

}
