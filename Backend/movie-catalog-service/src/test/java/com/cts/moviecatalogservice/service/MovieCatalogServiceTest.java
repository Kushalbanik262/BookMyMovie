package com.cts.moviecatalogservice.service;

import com.cts.moviecatalogservice.dto.AllMovieResponse;
import com.cts.moviecatalogservice.dto.MovieDto;
import com.cts.moviecatalogservice.dto.ShowingDto;
import com.cts.moviecatalogservice.exceptions.ResourceNotFoundException;
import com.cts.moviecatalogservice.model.Movie;
import com.cts.moviecatalogservice.model.Showing;
import com.cts.moviecatalogservice.model.Theater;
import com.cts.moviecatalogservice.repository.MovieRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.w3c.dom.stylesheets.LinkStyle;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MovieCatalogServiceTest {

    @Mock
    private MovieRepository repository;

    @InjectMocks
    private MovieCatalogService service;


    @BeforeEach
    void setUp() {

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getAllMovies() {
        List<Movie> movies = List.of(
                Movie.builder().id("MV101").genre("Horror").cast("SR Varun").title("Indumati").language("Hindi")
                .runtime(150).country("India").releaseDate(LocalDate.of(2022,5,5)).director("S Joseph")
                .trailerUrl("https://netflix.com/Indumati?cast=244$").build(),

                Movie.builder().id("MV102").genre("Comedy").cast("Pandav Tiwari").title("Golmal Active").language("Malyalam")
                        .runtime(150).country("India").releaseDate(LocalDate.of(2022,4,7)).director("MV Palli")
                        .trailerUrl("https://netflix.com/GolmalAction?cast=47$&MinesHash/oauth=454543").build());


        Mockito.when(repository.findAll()).thenReturn(movies);

             AllMovieResponse expected =  AllMovieResponse.builder().movies(movies.stream().map(m -> MovieDto.builder().id(m.getId()).title(m.getTitle()).description(m.getDescription())
                .releaseDate(m.getReleaseDate()).runtime(m.getRuntime()).genre(m.getGenre())
                .language(m.getLanguage()).country(m.getCountry()).director(m.getDirector()).cast(m.getCast())
                .rating(m.getRating()).posterUrl(m.getPosterUrl()).trailerUrl(m.getTrailerUrl()).build())
                .collect(Collectors.toList())).build();


         AllMovieResponse response = service.getAllMovies();
        assertEquals(response,expected);

    }

    @Test
    void getMovieById_FOUND() {
        String MovieId = "MV101";
        Movie movie =  Movie.builder().id("MV101").genre("Horror").cast("SR Varun").title("Indumati").language("Hindi")
                .runtime(150).country("India").releaseDate(LocalDate.of(2022,5,5)).director("S Joseph")
                .trailerUrl("https://netflix.com/Indumati?cast=244$")
                .shows(List.of(
                        Showing.builder().id("SH147").showTime("03:00AM").totalSeats(120).bookedSeats(0).theater(
                                Theater.builder().id("TH1478").location("Kolkata").build()).build()
                ))
                .build();

        Mockito.when(repository.findById(MovieId)).thenReturn(Optional.of(movie));

        MovieDto dto = MovieDto.builder().id(movie.getId()).title(movie.getTitle()).description(movie.getDescription())
                .releaseDate(movie.getReleaseDate()).runtime(movie.getRuntime()).genre(movie.getGenre())
                .language(movie.getLanguage()).country(movie.getCountry()).director(movie.getDirector())
                .cast(movie.getCast()).rating(movie.getRating()).posterUrl(movie.getPosterUrl())
                .trailerUrl(movie.getTrailerUrl()).shows(List.of(
                        Showing.builder().id("SH147").showTime("03:00AM").totalSeats(120).bookedSeats(0).theater(
                                Theater.builder().id("TH1478").location("Kolkata").build()).build()).stream()
                                .map(m -> ShowingDto.builder().id(m.getId()).name(m.getTheater().getName())
                                        .location(m.getTheater().getLocation()).showTime(m.getShowTime()).totalSeats(m.getTotalSeats())
                                        .bookedSeats(m.getBookedSeats()).build())
                                .collect(Collectors.toList())
                ).build();

        MovieDto currentDto = service.getMovieById(MovieId);

        assertEquals(dto,currentDto);

    }

    @Test
    void getMovieById_NOT_FOUND(){
        String MovieId = "MV101";
        Mockito.when(repository.findById(MovieId)).thenReturn(Optional.empty());

        Assertions.assertThrows(
                ResourceNotFoundException.class,
                ()->{service.getMovieById(MovieId);},
                "Can't Find any Movie with the Id");

    }

    @Test
    void getMoviesByName() {
        String name = "Galaxy";

        List<Movie> movies = List.of(
                Movie.builder().id("MV101").genre("Horror").cast("SR Varun").title("Indumati").language("Hindi")
                        .runtime(150).country("India").releaseDate(LocalDate.of(2023,5,5)).director("S Joseph")
                        .trailerUrl("https://netflix.com/Indumati?cast=244$")
                        .shows(List.of(
                                Showing.builder().id("SH147").showTime("03:00AM").totalSeats(120).bookedSeats(0).theater(
                                        Theater.builder().id("TH1478").location("Kolkata").build()).build()
                        ))
                        .build(),
                Movie.builder().id("MV104").genre("scifi").cast("GH. BUSH").title("God of Galaxy").language("English")
                        .runtime(180).country("USA").releaseDate(LocalDate.of(2023,4,15)).director("SR Watson")
                        .trailerUrl("https://netflix.com/GodOfGalaxy?wt=254/#sh/oauth=?43")
                        .shows(List.of(
                                Showing.builder().id("SH147").showTime("05:00AM").totalSeats(120).bookedSeats(0).theater(
                                        Theater.builder().id("TH1478").location("Kolkata").build()).build()
                        ))
                        .build()

        );

      AllMovieResponse expected =   AllMovieResponse.builder().movies(List.of(MovieDto.builder()
                .id(movies.get(1).getId()).
                title(movies.get(1).getTitle())
                .genre(movies.get(1).getGenre())
                .description(movies.get(1).getDescription())
                .posterUrl(movies.get(1).getPosterUrl())
                .rating(movies.get(1).getRating())
                .cast(movies.get(1).getCast())
                .country(movies.get(1).getCountry())
                .director(movies.get(1).getDirector())
                .releaseDate(movies.get(1).getReleaseDate())
                .trailerUrl(movies.get(1).getTrailerUrl())
                .build())).build();

      Mockito.when(repository.findMoviesByTitleContains(name)).thenReturn(
              List.of(
                      Movie.builder().id("MV104").genre("scifi").cast("GH. BUSH").title("God of Galaxy").language("English")
                              .runtime(180).country("USA").releaseDate(LocalDate.of(2023,4,15)).director("SR Watson")
                              .trailerUrl("https://netflix.com/GodOfGalaxy?wt=254/#sh/oauth=?43")
                              .shows(List.of(
                                      Showing.builder().id("SH147").showTime("05:00AM").totalSeats(120).bookedSeats(0).theater(
                                              Theater.builder().id("TH1478").location("Kolkata").build()).build()
                              ))
                              .build()
              )
      );

      AllMovieResponse response = service.getMovieByName(name);

      assertEquals(response,expected);

    }
}