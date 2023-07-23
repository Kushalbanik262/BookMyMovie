package com.cts.ticketbookingservice.controller;

import com.cts.ticketbookingservice.dto.BookTicketRequest;
import com.cts.ticketbookingservice.dto.Response;
import com.cts.ticketbookingservice.exceptions.InvalidTokenException;
import com.cts.ticketbookingservice.exceptions.ResourceNotFoundException;
import com.cts.ticketbookingservice.service.TicketBookingService;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TicketBookingControllerTest {

    @MockBean
    private TicketBookingService service;

    @Autowired
    private MockMvc mvc;


    @Test
    void bookTicket_SUCCESS() throws Exception {
        BookTicketRequest request = BookTicketRequest.builder().showingId("SH101").seats(4).build();
        String token = "TOK124#$%";

        Mockito.when(service.bookTicket(token,request)).thenReturn(Response.builder().status("BOOKED")
                .message("Ticked Booked Successfully").build());

        String expected = "{\"status\":\"BOOKED\",\"message\":\"Ticked Booked Successfully\"}";

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(request);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization",token);

        this.mvc.perform(post("/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .headers(headers)
                ).andDo(print()).andExpect(status().isOk())
                .andExpect(content().json(expected,false));
    }

    @Test
    void bookTicket_FAILURE_MORE_TICKETS() throws Exception {
        BookTicketRequest request = BookTicketRequest.builder().showingId("SH101").seats(10).build();
        String token = "TOK124#$%";

        Mockito.when(service.bookTicket(token,request)).thenReturn(Response.builder().status("BOOKED")
                .message("Ticked Booked Successfully").build());


        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(request);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization",token);

        this.mvc.perform(post("/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .headers(headers)
                ).andDo(print()).andExpect(status().is4xxClientError());
    }

    @Test
    void bookTicket_FAILURE_TOKEN_INVALID() throws Exception {
        BookTicketRequest request = BookTicketRequest.builder().showingId("SH101").seats(5).build();
        String token = "Bearer TOK124#$%";

        Mockito.when(service.bookTicket(token,request)).thenThrow(new InvalidTokenException("Invalid Token"));

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(request);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization",token);

        String expected = "{\"status\":\"error\",\"message\":\"Invalid Token\"}";

        this.mvc.perform(post("/book")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .headers(headers)
        ).andDo(print()).andExpect(status().is4xxClientError())
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(content().json(expected,false));
    }

    @Test
    void bookTicket_FAILURE_SHOW_NOT_AVAILABLE() throws Exception{
        BookTicketRequest request = BookTicketRequest.builder().showingId("SH101").seats(5).build();
        String token = "Bearer TOK124#$%";

        Mockito.when(service.bookTicket(token,request)).thenThrow(new ResourceNotFoundException("Showing Not Found"));

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(request);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization",token);

        String expected = "{\"status\":\"error\",\"message\":\"Showing Not Found\"}";

        this.mvc.perform(post("/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .headers(headers)
                ).andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(content().json(expected,false));
     }


}