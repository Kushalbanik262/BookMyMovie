package com.cts.adminservice.controller;

import com.cts.adminservice.dto.AddMovieRequest;
import com.cts.adminservice.dto.ShowingDto;
import com.cts.adminservice.exceptions.InvalidTokenException;
import com.cts.adminservice.exceptions.ResourceNotFoundException;
import com.cts.adminservice.model.Movie;
import com.cts.adminservice.service.AdminService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AdminControllerTest {


    @MockBean
    private AdminService service;

    @Autowired
    private MockMvc mvc;


    @Test
    void updateTicketStatus_SUCCESS() throws Exception {
        String ticketId = "TCK102$%ID";
        String newStatus = "CANCELLED";
        String tokenId = "TOK1234$#%34";


        Mockito.when(service.updateTicketStatus(tokenId,ticketId,newStatus)).thenReturn(true);
        String expected = "{\"status\":\"success\",\"message\":\"Ticket updated successfully\"}";


        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization",tokenId);

        this.mvc.perform(put("/update/"+ticketId+"/"+newStatus)
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(headers)
                ).andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(expected,false));
    }

    @Test
    void updateTicketStatus_FAILURE() throws Exception {
        String ticketId = "TCK102$%ID";
        String newStatus = "CANCELLED";
        String tokenId = "TOK1234$#%34";


        Mockito.when(service.updateTicketStatus(tokenId,ticketId,newStatus)).thenThrow(
                new ResourceNotFoundException("No ticket found with id:"+ticketId)
        );

        String expected = "{\"status\":\"error\",\"message\":\"No ticket found with id:TCK102$%ID\"}";


        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization",tokenId);

        this.mvc.perform(put("/update/"+ticketId+"/"+newStatus)
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(headers)
                ).andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().json(expected,false));
    }

//    @Test
//    void addMovie_SUCCESS() throws Exception {
//        String ticketId = "TCK102$%ID";
//        String newStatus = "CANCELLED";
//        String tokenId = "Bearer TOK1234$#%34";
//
//        AddMovieRequest request = AddMovieRequest.builder().cast("SH Batt").country("USA")
//                .description("A film related to fish").genre("Thriller").title("Dead sea").runtime(150)
//                .posterUrl("https://www.netflix.com/deadSea?=fsh").rating("3.4").director("Tom Watson")
//                .trailerUrl("https://www.netflix.com/deadSea?=fsh")
//                .language("English")
//                .releaseDate(LocalDate.now())
//                .shows(
//                        List.of(
//                                ShowingDto.builder().showTime("03.00 PM").totalSeats(120).theaterId("TH1245").build(),
//                                ShowingDto.builder().showTime("05.00 PM").totalSeats(98).theaterId("TH7854").build()
//                        )
//                )
//                .build();
//
//             Movie movie = Movie.builder().id("M" + "ID1234#$%").title(request.getTitle())
//                .description(request.getDescription()).releaseDate(request.getReleaseDate())
//                .runtime(request.getRuntime()).genre(request.getGenre()).language(request.getLanguage())
//                .country(request.getCountry()).director(request.getDescription()).cast(request.getCast())
//                .rating(request.getRating()).posterUrl(request.getPosterUrl()).trailerUrl(request.getTrailerUrl())
//                .build();
//
//
//        Mockito.when(service.addMovie(tokenId,request)).thenReturn(movie);
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Authorization",tokenId);
//
//
//        String expected = "";
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        String json = objectMapper.writeValueAsString(request);
//
//        this.mvc.perform(post("/addmovie")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(json)
//                        .headers(headers)
//                ).andDo(print())
//                .andExpect(status().is2xxSuccessful())
//                .andExpect(content().json(expected,false));
//    }

    @Test
    void addMovie_FAILURE() throws Exception {
        String ticketId = "TCK102$%ID";
        String newStatus = "CANCELLED";
        String tokenId = "Bearer TOK1234$#%34";

        AddMovieRequest request = AddMovieRequest.builder().cast("SH Batt").country("USA")
                .description("A film related to fish").genre("Thriller").title("Dead sea").runtime(150)
                .posterUrl("https://www.netflix.com/deadSea?=fsh").rating("3.4").director("Tom Watson")
                .trailerUrl("https://www.netflix.com/deadSea?=fsh")
                .language("English")
                .shows(
                        List.of(
                                ShowingDto.builder().showTime("03.00 PM").totalSeats(120).theaterId("TH1245").build(),
                                ShowingDto.builder().showTime("05.00 PM").totalSeats(98).theaterId("TH7854").build()
                        )
                )
                .build();

        Movie movie = Movie.builder().id("M" + "ID1234#$%").title(request.getTitle())
                .description(request.getDescription()).releaseDate(request.getReleaseDate())
                .runtime(request.getRuntime()).genre(request.getGenre()).language(request.getLanguage())
                .country(request.getCountry()).director(request.getDescription()).cast(request.getCast())
                .rating(request.getRating()).posterUrl(request.getPosterUrl()).trailerUrl(request.getTrailerUrl())
                .build();


        Mockito.when(service.addMovie(tokenId,request)).thenReturn(movie);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization",tokenId);


        String expected = "{\"status\":\"error\",\"message\":\"Release date is required\"}";

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(request);

        this.mvc.perform(post("/addmovie")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .headers(headers)
                ).andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().json(expected,false));
    }


    @Test
    void deleteMovie_SUCCESS() throws Exception {
        String token = "Bearer TOK124$%#";
        String movieId = "MVE1234$%#";

        Mockito.when(service.deleteMovie(token,movieId)).thenReturn(true);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization",token);

        String expected = "{\"status\":\"success\",\"message\":\"Movie deleted successfully\"}";

        this.mvc.perform(delete("/delete/"+movieId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(headers)
                ).andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(expected,false));
    }

    @Test
    void deleteMovie_FAILURE() throws Exception {
        String token = "Bearer TOK124$%#";
        String movieId = "MVE1234$%#";

        Mockito.when(service.deleteMovie(any(String.class),any(String.class))).thenThrow(new ResourceNotFoundException("Movie Not Found"));
        String expected = "{\"status\":\"error\",\"message\":\"Movie Not Found\"}";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization",token);

        this.mvc.perform(delete("/delete/"+movieId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(headers)
                ).andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().json(expected,false));
    }
}