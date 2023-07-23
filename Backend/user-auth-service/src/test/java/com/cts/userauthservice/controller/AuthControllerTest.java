package com.cts.userauthservice.controller;

import com.cts.userauthservice.dto.*;
import com.cts.userauthservice.exceptions.ResourceNotFoundException;
import com.cts.userauthservice.model.Role;
import com.cts.userauthservice.model.SecretQuestion;
import com.cts.userauthservice.model.User;
import com.cts.userauthservice.service.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AuthControllerTest {

    @Mock
    private AuthService service;

    @Mock
    private BindingResult bResult;

    @InjectMocks
    private AuthController controller;


    @Test
    void registerSuccess() {
        RegistrationRequest registrationRequest = RegistrationRequest.builder().email("kushalbanik93@gmail.com").firstName("kushal")
                .lastName("banik").secretQuestionId((long)1).answerToSecretQuestion("michu").build();

        User user = User.builder().userId("UID1234").role(Role.CUSTOMER).firstName("kushal").lastName("banik")
                        .email("kushalbanik93@gmail.com").password("Kush@124").build();

        Mockito.when(service.register(registrationRequest)).thenReturn(user);

        Mockito.when(bResult.hasErrors()).thenReturn(false);

        ResponseEntity<Response> response =  controller.register(registrationRequest,bResult);
        assertTrue(response.getStatusCode().is2xxSuccessful());
    }

    @Test
    void registerFail(){
        RegistrationRequest registrationRequest = RegistrationRequest.builder().email("kushalbanik93@gmail.com").firstName("kushal")
                .lastName("banik").secretQuestionId((long)1).answerToSecretQuestion("michu").build();

        User user = User.builder().userId("UID1234").role(Role.CUSTOMER).firstName("kushal").lastName("banik")
                .email("kushalbanik93@gmail.com").password("kush124").build();

        Mockito.when(service.register(registrationRequest)).thenReturn(user);

        Mockito.when(bResult.hasErrors()).thenReturn(true);
        Mockito.when(bResult.getAllErrors()).thenReturn(List.of(new ObjectError("RegistrationRequest",
                "Password is Not according to specification")));

        ResponseEntity<Response> response =  controller.register(registrationRequest,bResult);
        assertTrue(response.getStatusCode().is4xxClientError());
    }

    @Test
    void loginSuccess() {
        LoginRequest request = LoginRequest.builder().email("kushalbanik93@gmail.com").password("Kushal1234").build();
        Mockito.when(bResult.hasErrors()).thenReturn(false);

        LoginResponse response = LoginResponse.builder().email("kushalbanik93@gmail.com")
                .jwtToken("TOK1234").firstName("kushal").lastName("banik").role("CUSTOMER").build();

        Mockito.when(service.login(request)).thenReturn(response);

        ResponseEntity<LoginResponse> responseCon = (ResponseEntity<LoginResponse>) controller.login(request,bResult);

        assertTrue(responseCon.getStatusCode().is2xxSuccessful());

        assertEquals(response,responseCon.getBody());
    }

//    @Test
//    void loginFail() {
//        LoginRequest request = LoginRequest.builder().email("kushalbanik93@gmail.com").password("Kushal1234").build();
//        Mockito.when(bResult.hasErrors()).thenReturn(false);
//        Mockito.when(service.login(request)).thenThrow(new ResourceNotFoundException("Password not matched"));
//
//        ResponseEntity<Response> response = (ResponseEntity<Response>) controller.login(request,bResult);
//
//        assertTrue(response.getStatusCode().is4xxClientError());
//    }

    @Test
    void forgotPassword() {
        PasswordChangeRequest passwordChangeRequest = PasswordChangeRequest.builder()
                .answer("michu").newPassword("kushalB@1234").securityQuestionId((long)1).build();
        String userId = "UI#1234$";

        User user = User.builder().userId(userId).role(Role.CUSTOMER)
                .secretQuestion(SecretQuestion.builder().id((long)1).question("What is your pet's name").build())
                        .firstName("kushal").lastName("banik").answerToSecretQuestion("michu")
                        .email("kushalbanik93@gmail.com").password("HSA@#$%").build();

        Mockito.when(service.forgotPassword(userId,passwordChangeRequest)).thenReturn(user);

        ResponseEntity<Response> response =  controller.forgotPassword(userId,passwordChangeRequest,bResult);

        assertTrue(response.getStatusCode().is2xxSuccessful());
    }

    @Test
    void validateAuthToken_VALID() {
        String JwtToken = "Bearer ##$^1455dfs";

        ValidationDto validationDto = ValidationDto.builder().status(true).userId("UID124sadff").role("CUSTOMER").build();
        Mockito.when(service.validateAuthToken("Bearer ##$^1455dfs")).thenReturn(validationDto);

        ResponseEntity<ValidationDto> actual = controller.validateAuthToken(JwtToken);

        assertEquals(actual.getBody(),validationDto);

        assertTrue(actual.getStatusCode().is2xxSuccessful());

    }

    @Test
    void validateAuthToken_INVALID() {
        String JwtToken = "Bearer ##$^1455dfs";

        ValidationDto validationDto = ValidationDto.builder().status(false).build();
        Mockito.when(service.validateAuthToken("Bearer ##$^1455dfs")).thenReturn(validationDto);

        ResponseEntity<ValidationDto> actual = controller.validateAuthToken(JwtToken);


        assertTrue(actual.getStatusCode().is4xxClientError());
    }
}