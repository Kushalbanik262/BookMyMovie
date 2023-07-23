package com.cts.ticketbookingservice.service;

import com.cts.ticketbookingservice.dto.BookTicketRequest;
import com.cts.ticketbookingservice.dto.Response;
import com.cts.ticketbookingservice.dto.ValidationDto;
import com.cts.ticketbookingservice.exceptions.InvalidTokenException;
import com.cts.ticketbookingservice.feign.AuthClient;
import com.cts.ticketbookingservice.model.Role;
import com.cts.ticketbookingservice.model.Showing;
import com.cts.ticketbookingservice.model.User;
import com.cts.ticketbookingservice.repository.ShowingRepository;
import com.cts.ticketbookingservice.repository.TicketBookingRepository;
import com.cts.ticketbookingservice.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TicketBookingServiceTest {
    @Mock
    private AuthClient authClient;
    @Mock
    private ShowingRepository showingRepository;
    @Mock
    private TicketBookingRepository bookingRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TicketBookingService service;

    @Test
    void bookTicket_SUCCESS() {
        BookTicketRequest request = BookTicketRequest.builder().showingId("SH101").seats(10).build();
        String token = "TOK124#$%";
        Showing show = Showing.builder().id("SH101").bookedSeats(0).showTime("03:00PM").totalSeats(120).build();

        ValidationDto validationDto = ValidationDto.builder().status(true)
                .role("CUSTOMER").userId("UID1234#3").build();

        User user = User.builder().userId("UID1234#3").role(Role.CUSTOMER)
                .email("kushalbanik93@gmail.com").firstName("kushal").lastName("banik").build();


        Mockito.when(authClient.validateAuthToken(token)).thenReturn(validationDto);
        Mockito.when(showingRepository.findById("SH101")).thenReturn(Optional.of(show));

        Response response = service.bookTicket(token,request);

        assertEquals(response, Response.builder().status("success").message("Ticket booking successfull").build());
    }


    @Test
    void bookTicket_FAILURE(){
        BookTicketRequest request = BookTicketRequest.builder().showingId("SH101").seats(10).build();
        String token = "TOK124#$%";
        Showing show = Showing.builder().id("SH101").bookedSeats(0).showTime("03:00PM").totalSeats(120).build();

        ValidationDto validationDto = ValidationDto.builder().status(false)
                .role("CUSTOMER").userId("UID1234#3").build();

        Mockito.when(authClient.validateAuthToken(token)).thenReturn(validationDto);

        Assertions.assertThrows(
                InvalidTokenException.class,
                ()->{service.bookTicket(token,request);},
                "Sorry the token is invalid");
    }

    @Test
    void Ticket_NOT_AVAILABLE_TICKET_TEST(){
        BookTicketRequest request = BookTicketRequest.builder().showingId("SH101").seats(11).build();
        String token = "TOK124#$%";
        Showing show = Showing.builder().id("SH101").bookedSeats(110).showTime("03:00PM").totalSeats(120).build();

        ValidationDto validationDto = ValidationDto.builder().status(true)
                .role("CUSTOMER").userId("UID1234#3").build();

        User user = User.builder().userId("UID1234#3").role(Role.CUSTOMER)
                .email("kushalbanik93@gmail.com").firstName("kushal").lastName("banik").build();


        Mockito.when(authClient.validateAuthToken(token)).thenReturn(validationDto);
        Mockito.when(showingRepository.findById("SH101")).thenReturn(Optional.of(show));



        Assertions.assertThrows(
                RuntimeException.class,
                ()->{service.bookTicket(token,request);},
                "Sorry the No Seats are available");
    }
}