package com.cts.moviecatalogservice.repository;



import com.cts.moviecatalogservice.model.Movie;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends MongoRepository<Movie, String> {
    public List<Movie>  findMoviesByTitleContains(String keyWord);
}
