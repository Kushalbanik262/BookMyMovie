package com.cts.moviecatalogservice.controller;

import com.cts.moviecatalogservice.dto.AllMovieResponse;
import com.cts.moviecatalogservice.dto.MovieDto;
import com.cts.moviecatalogservice.dto.ShowingDto;
import com.cts.moviecatalogservice.exceptions.ResourceNotFoundException;
import com.cts.moviecatalogservice.model.Movie;
import com.cts.moviecatalogservice.service.MovieCatalogService;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class MovieCatalogControllerTest {

    @MockBean
    private MovieCatalogService service;

    @Autowired
    private MockMvc mvc;

    @Test
    void getAllMovies() throws Exception {
        List<Movie> movies = List.of(
                Movie.builder().id("MV101").genre("Horror").cast("SR Varun").title("Indumati").language("Hindi")
                        .runtime(150).country("India").releaseDate(LocalDate.of(2022,5,5)).director("S Joseph")
                        .trailerUrl("https://netflix.com/Indumati?cast=244$").build(),

                Movie.builder().id("MV102").genre("Comedy").cast("Pandav Tiwari").title("Golmal Active").language("Malyalam")
                        .runtime(150).country("India").releaseDate(LocalDate.of(2022,4,7)).director("MV Palli")
                        .trailerUrl("https://netflix.com/GolmalAction?cast=47$&MinesHash/oauth=454543").build());

        AllMovieResponse expected =  AllMovieResponse.builder().movies(movies.stream().map(m -> MovieDto.builder().id(m.getId()).title(m.getTitle()).description(m.getDescription())
                        .releaseDate(m.getReleaseDate()).runtime(m.getRuntime()).genre(m.getGenre())
                        .language(m.getLanguage()).country(m.getCountry()).director(m.getDirector()).cast(m.getCast())
                        .rating(m.getRating()).posterUrl(m.getPosterUrl()).trailerUrl(m.getTrailerUrl()).build())
                .collect(Collectors.toList())).build();

        String expectedRe = "{\"movies\":[{\"id\":\"MV101\",\"title\":\"Indumati\",\"description\":null,\"releaseDate\":\"2022-05-05\",\"runtime\":150,\"genre\":\"Horror\",\"language\":\"Hindi\",\"country\":\"India\",\"director\":\"S Joseph\",\"cast\":\"SR Varun\",\"rating\":null,\"posterUrl\":null,\"trailerUrl\":\"https://netflix.com/Indumati?cast=244$\",\"shows\":null},{\"id\":\"MV102\",\"title\":\"Golmal Active\",\"description\":null,\"releaseDate\":\"2022-04-07\",\"runtime\":150,\"genre\":\"Comedy\",\"language\":\"Malyalam\",\"country\":\"India\",\"director\":\"MV Palli\",\"cast\":\"Pandav Tiwari\",\"rating\":null,\"posterUrl\":null,\"trailerUrl\":\"https://netflix.com/GolmalAction?cast=47$&MinesHash/oauth=454543\",\"shows\":null}]}";

        Mockito.when(service.getAllMovies()).thenReturn(expected);
        //System.out.println(result.getResponse().getContentAsString());
        this.mvc.perform(get("/all")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().json(expectedRe,false));
    }

    @Test
    void getMovieById() throws Exception {
        Movie movie = Movie.builder().id("MV102").genre("Comedy").cast("Pandav Tiwari").title("Golmal Active").language("Malyalam")
                .runtime(150).country("India").releaseDate(LocalDate.of(2022,4,7)).director("MV Palli")
                .trailerUrl("https://netflix.com/GolmalAction?cast=47$&MinesHash/oauth=454543").build();

        String expected = "{\"id\":\"MV102\",\"title\":\"Golmal Active\",\"description\":null,\"releaseDate\":\"2022-04-07\",\"runtime\":150,\"genre\":\"Comedy\",\"language\":\"Malyalam\",\"country\":\"India\",\"director\":\"MV Palli\",\"cast\":\"Pandav Tiwari\",\"rating\":null,\"posterUrl\":null,\"trailerUrl\":\"https://netflix.com/GolmalAction?cast=47$&MinesHash/oauth=454543\",\"shows\":null}";

        List<ShowingDto> shows = null;

        MovieDto dto = MovieDto.builder().id(movie.getId()).title(movie.getTitle()).description(movie.getDescription())
                .releaseDate(movie.getReleaseDate()).runtime(movie.getRuntime()).genre(movie.getGenre())
                .language(movie.getLanguage()).country(movie.getCountry()).director(movie.getDirector())
                .cast(movie.getCast()).rating(movie.getRating()).posterUrl(movie.getPosterUrl())
                .trailerUrl(movie.getTrailerUrl()).shows(shows).build();


        Mockito.when(service.getMovieById(movie.getId())).thenReturn(dto);


        this.mvc.perform(get("/movies/search/id/"+movie.getId())).andDo(print()).andExpect(status().isOk())
                .andExpect(content().json(expected,false));
    }

    @Test
    void getMovieById_FAIL() throws Exception {
        Movie movie = Movie.builder().id("MV102").genre("Comedy").cast("Pandav Tiwari").title("Golmal Active").language("Malyalam")
                .runtime(150).country("India").releaseDate(LocalDate.of(2022,4,7)).director("MV Palli")
                .trailerUrl("https://netflix.com/GolmalAction?cast=47$&MinesHash/oauth=454543").build();

        String expected = "{\"id\":\"MV102\",\"title\":\"Golmal Active\",\"description\":null,\"releaseDate\":\"2022-04-07\",\"runtime\":150,\"genre\":\"Comedy\",\"language\":\"Malyalam\",\"country\":\"India\",\"director\":\"MV Palli\",\"cast\":\"Pandav Tiwari\",\"rating\":null,\"posterUrl\":null,\"trailerUrl\":\"https://netflix.com/GolmalAction?cast=47$&MinesHash/oauth=454543\",\"shows\":null}";

        List<ShowingDto> shows = null;

        MovieDto dto = MovieDto.builder().id(movie.getId()).title(movie.getTitle()).description(movie.getDescription())
                .releaseDate(movie.getReleaseDate()).runtime(movie.getRuntime()).genre(movie.getGenre())
                .language(movie.getLanguage()).country(movie.getCountry()).director(movie.getDirector())
                .cast(movie.getCast()).rating(movie.getRating()).posterUrl(movie.getPosterUrl())
                .trailerUrl(movie.getTrailerUrl()).shows(shows).build();


        Mockito.when(service.getMovieById(movie.getId())).thenThrow(new ResourceNotFoundException("Movie Not Found"));


        this.mvc.perform(get("/movies/search/id/"+movie.getId())).andDo(print()).andExpect(status().is4xxClientError());
    }

    @Test
    void getAllMatchingMovies() throws Exception {
        List<Movie> movies = List.of(Movie.builder().id("MV102").genre("Comedy").cast("Pandav Tiwari").title("Golmal Active").language("Malyalam")
                .runtime(150).country("India").releaseDate(LocalDate.of(2022,4,7)).director("MV Palli")
                .trailerUrl("https://netflix.com/GolmalAction?cast=47$&MinesHash/oauth=454543").build());

        List<MovieDto> response_Movies =  movies.stream().map(m->MovieDto.builder()
                .id(m.getId()).
                title(m.getTitle())
                .genre(m.getGenre())
                .description(m.getDescription())
                .posterUrl(m.getPosterUrl())
                .rating(m.getRating())
                .cast(m.getCast())
                .country(m.getCountry())
                .director(m.getDirector())
                .releaseDate(m.getReleaseDate())
                .trailerUrl(m.getTrailerUrl())
                .build()
        ).collect(Collectors.toList());

        AllMovieResponse response = AllMovieResponse.builder().movies(response_Movies).build();


        Mockito.when(service.getMovieByName("Tiwari")).thenReturn(response);

        String expected = "{\"movies\":[{\"id\":\"MV102\",\"title\":\"Golmal Active\",\"description\":null,\"releaseDate\":\"2022-04-07\",\"runtime\":0,\"genre\":\"Comedy\",\"language\":null,\"country\":\"India\",\"director\":\"MV Palli\",\"cast\":\"Pandav Tiwari\",\"rating\":null,\"posterUrl\":null,\"trailerUrl\":\"https://netflix.com/GolmalAction?cast=47$&MinesHash/oauth=454543\",\"shows\":null}]}";

        this.mvc.perform(get("/movies/search/name/Tiwari")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().json(expected,false));

    }
}