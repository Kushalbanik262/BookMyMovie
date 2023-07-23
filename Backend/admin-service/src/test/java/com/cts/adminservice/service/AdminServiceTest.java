package com.cts.adminservice.service;

import com.cts.adminservice.dto.AddMovieRequest;
import com.cts.adminservice.dto.ShowingDto;
import com.cts.adminservice.dto.ValidationDto;
import com.cts.adminservice.exceptions.InvalidTokenException;
import com.cts.adminservice.feign.AuthClient;
import com.cts.adminservice.model.Movie;
import com.cts.adminservice.model.Theater;
import com.cts.adminservice.model.TicketBooking;
import com.cts.adminservice.repository.MovieRepository;
import com.cts.adminservice.repository.ShowingRepository;
import com.cts.adminservice.repository.TheaterRepository;
import com.cts.adminservice.repository.TicketBookingRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.InvalidIsolationLevelException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @Mock
    private AuthClient authClient;

    @Mock
    private MovieRepository movieRepository;
    @Mock
    private ShowingRepository showingRepository;
    @Mock
    private TheaterRepository theaterRepository;
    @Mock
    private TicketBookingRepository bookingRepository;
    @Mock
    private MovieProducer movieProducer;


    @InjectMocks
    private AdminService service;



    @Test
    void updateTicketStatus_SUCCESS() {
        String token = "TOK1234#$"; String ticketId = "TIC12345@#"; String newStatus = "CANCELLED";
        ValidationDto dto = ValidationDto.builder().role("ADMIN").status(true).userId("UID1234354").build();


        TicketBooking ticket = TicketBooking.builder().id(ticketId).status("SUCCESS").numSeats(10).build();

        Mockito.when(authClient.validateAuthToken(token)).thenReturn(dto);
        Mockito.when(bookingRepository.findById(ticketId)).thenReturn(Optional.of(ticket));

        assertTrue(service.updateTicketStatus(token,ticketId,newStatus));
    }

    @Test
    void updateTicketStatus_FAILURE() {
        String token = "TOK1234#$"; String ticketId = "TIC12345@#"; String newStatus = "CANCELLED";
        ValidationDto dto = ValidationDto.builder().role("CUSTOMER").status(true).userId("UID1234354").build();


        TicketBooking ticket = TicketBooking.builder().id(ticketId).status("SUCCESS").numSeats(10).build();

        Mockito.when(authClient.validateAuthToken(token)).thenReturn(dto);

        Assertions.assertThrows(
                InvalidTokenException.class,
                ()->{service.updateTicketStatus(token,ticketId,newStatus);},
                "Only admin can update ticket status");

    }

    @Test
    void addMovie_success() {
        String token = "TOK1234#$47";


        AddMovieRequest request = AddMovieRequest.builder().cast("SH Batt").country("USA")
                .description("A film related to fish").genre("Thriller").title("Dead sea").runtime(150)
                .posterUrl("https://www.netflix.com/deadSea?=fsh").rating("3.4").director("Tom Watson")
                .trailerUrl("https://www.netflix.com/deadSea?=fsh")
                .releaseDate(LocalDate.of(2023,8,10)).language("English")
                .shows(
                        List.of(
                                ShowingDto.builder().showTime("03.00 PM").totalSeats(120).theaterId("TH1245").build(),
                                ShowingDto.builder().showTime("05.00 PM").totalSeats(98).theaterId("TH7854").build()
                        )
                )
                .build();

        Movie expected = Movie.builder().id("M" + "12345#$%").title(request.getTitle())
                .description(request.getDescription()).releaseDate(request.getReleaseDate())
                .runtime(request.getRuntime()).genre(request.getGenre()).language(request.getLanguage())
                .country(request.getCountry()).director(request.getDescription()).cast(request.getCast())
                .rating(request.getRating()).posterUrl(request.getPosterUrl()).trailerUrl(request.getTrailerUrl())
                .build();

        ValidationDto dto = ValidationDto.builder().role("ADMIN").status(true).userId("UID1234354").build();

        Mockito.when(authClient.validateAuthToken(token)).thenReturn(dto);
        Mockito.when(theaterRepository.findById("TH1245")).thenReturn(Optional.of(
                Theater.builder().id("TH1245").name("JAMINI HALL").location("Pune").build()
        ));

        Mockito.when(theaterRepository.findById("TH7854")).thenReturn(Optional.of(
                Theater.builder().id("TH7854").name("MAXIMUS").location("Bangalore").build()
        ));



        Movie returned = service.addMovie(token,request);
        expected.setId(returned.getId());
        assertEquals(returned,expected);
    }

    @Test
    void addMovie_Fail() {
        String token = "TOK1234#$47";


        AddMovieRequest request = AddMovieRequest.builder().cast("SH Batt").country("USA")
                .description("A film related to fish").genre("Thriller").title("Dead sea").runtime(150)
                .posterUrl("https://www.netflix.com/deadSea?=fsh").rating("3.4").director("Tom Watson")
                .trailerUrl("https://www.netflix.com/deadSea?=fsh")
                .releaseDate(LocalDate.of(2023,8,10)).language("English")
                .shows(
                        List.of(
                                ShowingDto.builder().showTime("03.00 PM").totalSeats(120).theaterId("TH1245").build(),
                                ShowingDto.builder().showTime("05.00 PM").totalSeats(98).theaterId("TH7854").build()
                        )
                )
                .build();


        ValidationDto dto = ValidationDto.builder().role("CUSTOMER").status(true).userId("UID1234354").build();

        Mockito.when(authClient.validateAuthToken(token)).thenReturn(dto);

        Assertions.assertThrows(
                InvalidIsolationLevelException.class,
                ()->{service.addMovie(token,request);},
                "Only admin can add new movie");

    }


    @Test
    void deleteMovie_SUCCESS() {
        String token = "TOK1234#$47";
        String movieId = "MVE124#$%";

        ValidationDto dto = ValidationDto.builder().role("ADMIN").status(true).userId("UID1234354").build();

        Movie movie = Movie.builder().id("MVE124#$%").cast("SH Batt").country("USA")
                .description("A film related to fish").genre("Thriller").title("Dead sea").runtime(150)
                .posterUrl("https://www.netflix.com/deadSea?=fsh").rating("3.4").director("Tom Watson")
                .trailerUrl("https://www.netflix.com/deadSea?=fsh")
                .releaseDate(LocalDate.of(2023,8,10)).language("English")
                .build();

        Mockito.when(movieRepository.findById(movieId)).thenReturn(Optional.of(movie));
        Mockito.when(authClient.validateAuthToken(token)).thenReturn(dto);

        assertTrue(service.deleteMovie(token,movieId));
    }

    @Test
    void deleteMovie_FAIL(){
        String token = "TOK1234#$47";
        String movieId = "MVE124#$%";

        ValidationDto dto = ValidationDto.builder().role("CUSTOMER").status(true).userId("UID1234354").build();

        Mockito.when(authClient.validateAuthToken(token)).thenReturn(dto);

        Assertions.assertThrows(
                InvalidTokenException.class,
                ()->{service.deleteMovie(token,movieId);},
                "Only admin can perform delete movie action");
    }
}