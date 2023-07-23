package com.cts.userauthservice.service;

import com.cts.userauthservice.dto.*;
import com.cts.userauthservice.exceptions.EmailAlreadyExistsException;
import com.cts.userauthservice.model.Role;
import com.cts.userauthservice.model.SecretQuestion;
import com.cts.userauthservice.model.User;
import com.cts.userauthservice.repository.SecretQuestionRepository;
import com.cts.userauthservice.repository.UserRepository;
import com.cts.userauthservice.util.JwtProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AuthServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private SecretQuestionRepository questionRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtProvider jwtProvider;



    @InjectMocks
    private AuthService service;

    @BeforeEach
    void setUp() {

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void InvalidRegister(){
        RegistrationRequest registrationRequest = RegistrationRequest.builder().email("kushalbanik93@gmail.com").firstName("kushal")
                .lastName("banik").secretQuestionId((long)1).answerToSecretQuestion("michu").build();

        Mockito.when(userRepository.existsByEmail(registrationRequest.getEmail())).thenReturn(true);
        Assertions.assertThrows(EmailAlreadyExistsException.class,()->{service.register(registrationRequest);},
                "Email Already Exist Exception Expected");
    }


    @Test
    void SuccessRegister() {
        RegistrationRequest registrationRequest = RegistrationRequest.builder().email("kushalbanik93@gmail.com")
                .firstName("kushal").password("KushalB@1234")
                .lastName("banik").secretQuestionId((long)1).answerToSecretQuestion("michu").build();

        //User Not Registered
        Mockito.when(userRepository.existsByEmail(registrationRequest.getEmail())).thenReturn(false);
        SecretQuestion question = SecretQuestion.builder().id((long)1).question("what is your pet's name").build();
        //Getting The security Question
        Mockito.when(questionRepository.findById(registrationRequest.getSecretQuestionId())).thenReturn(Optional.of(question));


        Mockito.when(passwordEncoder.encode(registrationRequest.getPassword())).thenReturn("ABCDXYZ1234");
        //Mockito.when(service.generateUserId()).thenReturn("UID1234");


        User user = service.register(registrationRequest);

        //The User We have Created
        User expected = User.builder().userId(user.getUserId()).role(Role.CUSTOMER).secretQuestion(question)
                        .answerToSecretQuestion("michu")
                        .email("kushalbanik93@gmail.com").firstName("kushal").lastName("banik").password("ABCDXYZ1234").build();

        assertEquals(user,expected);
    }

    @Test
    void Login() {
        LoginRequest loginRequest = LoginRequest.builder().email("kushalbanik93@gmail.com").password("kushal12").build();
        User expected = User.builder().userId("UID1234").role(Role.CUSTOMER)
                .answerToSecretQuestion("michu")
                .email("kushalbanik93@gmail.com").firstName("kushal").lastName("banik").password("ABCDXYZ1234").build();


        Authentication authenticate = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        Mockito.when(authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()))).thenReturn(authenticate);

        Mockito.when(jwtProvider.generateToken(authenticate)).thenReturn("HASHCODE1234");
        Mockito.when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.of(expected));

        LoginResponse loginResponse = LoginResponse.builder().email("kushalbanik93@gmail.com").userId("UID1234").lastName("banik").firstName("kushal")
                .role("CUSTOMER")
                .jwtToken("HASHCODE1234").build();

        LoginResponse fromService = service.login(loginRequest);

        assertEquals(fromService,loginResponse);
    }

//
    @Test
    void forgotPassword() {
        PasswordChangeRequest change = PasswordChangeRequest.builder().securityQuestionId((long)1)
                .answer("michu").newPassword("kushaL1234").build();

        User user = User.builder().userId("UID1234").firstName("kushal").lastName("banik")
                    .email("kushalbanik93@gmail.com").answerToSecretQuestion("michu")
                    .secretQuestion(SecretQuestion.builder().id((long)1).question("Your pet's name").build())
                    .build();

        String UserId = "UID1234";

        Mockito.when(userRepository.findById(UserId)).thenReturn(Optional.of(user));

        Mockito.when(passwordEncoder.encode(change.getNewPassword())).thenReturn("$NEW#HASH");



        User currentUser = service.forgotPassword(UserId,change);

        assertEquals(currentUser.getPassword(),"$NEW#HASH");

    }
    @Test
    void forgotPasswordFailed() {
        PasswordChangeRequest change = PasswordChangeRequest.builder().securityQuestionId((long)1)
                .answer("michu").newPassword("kushaL1234").build();

        User user = User.builder().userId("UID1234").firstName("kushal").lastName("banik")
                .email("kushalbanik93@gmail.com").answerToSecretQuestion("mikhu")
                .secretQuestion(SecretQuestion.builder().id((long)1).question("Your pet's name").build())
                .build();

        String UserId = "UID1234";

        Mockito.when(userRepository.findById(UserId)).thenReturn(Optional.of(user));

        Mockito.when(passwordEncoder.encode(change.getNewPassword())).thenReturn("$NEW#HASH");


        Assertions.assertThrows(
                RuntimeException.class,
                ()->{service.forgotPassword(UserId,change);},
                "Secret question or answer is incorrect");
    }


    @Test
    void validateAuthToken_VALID() {
        String token = "Bearer $3sdfjksdffv";
        User user = User.builder().firstName("kushal").lastName("banik").userId("UID1234").role(Role.CUSTOMER).build();

        Mockito.when(jwtProvider.extractSubject("$3sdfjksdffv")).thenReturn("kushalbanik93@gmail.com");
        Mockito.when(userRepository.findByEmail("kushalbanik93@gmail.com")).thenReturn(Optional.of(user));

        ValidationDto expectedDto = ValidationDto.builder().role("CUSTOMER").userId("UID1234").status(true).build();

        ValidationDto resDto = service.validateAuthToken(token);

        assertEquals(expectedDto,resDto);
    }

    @Test
    void validateAuthToken_INVALID() {
        String token = "Bearer $3sdfjksdffv";
        User user = User.builder().firstName("kushal").lastName("banik").userId("UID1234").role(Role.CUSTOMER).build();

        Mockito.when(jwtProvider.extractSubject("$3sdfjksdffv")).thenReturn("kushalbanik93@gmail.com");
        Mockito.when(userRepository.findByEmail("kushalbanik93@gmail.com")).thenReturn(null);

        ValidationDto response = new ValidationDto();
       response.setStatus(false);

        ValidationDto resDto = service.validateAuthToken(token);

        assertEquals(response,resDto);
    }
}