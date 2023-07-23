package com.cts.userauthservice.service;

import java.util.UUID;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.ResourceAccessException;

import com.cts.userauthservice.dto.LoginRequest;
import com.cts.userauthservice.dto.LoginResponse;
import com.cts.userauthservice.dto.PasswordChangeRequest;
import com.cts.userauthservice.dto.RegistrationRequest;
import com.cts.userauthservice.dto.ValidationDto;
import com.cts.userauthservice.exceptions.EmailAlreadyExistsException;
import com.cts.userauthservice.exceptions.ResourceNotFoundException;
import com.cts.userauthservice.model.Role;
import com.cts.userauthservice.model.SecretQuestion;
import com.cts.userauthservice.model.User;
import com.cts.userauthservice.repository.SecretQuestionRepository;
import com.cts.userauthservice.repository.UserRepository;
import com.cts.userauthservice.util.JwtProvider;

@Service
@Slf4j
public class AuthService {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private SecretQuestionRepository questionRepository;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private JwtProvider jwtProvider;

	@Transactional
	public User register(RegistrationRequest request) {
		log.info("Registration Request:{}",request);
		if (userRepository.existsByEmail(request.getEmail())) {
			throw new EmailAlreadyExistsException("You email: " + request.getEmail() + " already exists");
		}
		SecretQuestion question = questionRepository.findById(request.getSecretQuestionId())
				.orElseThrow(() -> new ResourceAccessException(
						"No secret question found with id: " + request.getSecretQuestionId()));

		User user = User.builder().userId(generateUserId()).firstName(request.getFirstName())
				.lastName(request.getLastName()).email(request.getEmail())
				.password(passwordEncoder.encode(request.getPassword())).role(Role.CUSTOMER).secretQuestion(question)
				.answerToSecretQuestion(request.getAnswerToSecretQuestion()).build();

		userRepository.save(user);
		return user;
	}

	@Transactional(readOnly = true)
	public LoginResponse login(LoginRequest request) {
		log.info("Login Request:{}",request);
		Authentication authenticate = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authenticate);
		User user = userRepository.findByEmail(request.getEmail()).orElse(null);
		String jwtToken = jwtProvider.generateToken(authenticate);
		return LoginResponse.builder().email(request.getEmail()).userId(user.getUserId()).firstName(user.getFirstName())
				.lastName(user.getLastName()).role(user.getRole().toString()).jwtToken(jwtToken).build();
	}

	@Transactional
	public User forgotPassword(String userid, PasswordChangeRequest request) {
		User user = userRepository.findById(userid)
				.orElseThrow(() -> new ResourceNotFoundException("No user found with id: " + userid));
		if (user.getSecretQuestion().getId() != request.getSecurityQuestionId()
				|| !user.getAnswerToSecretQuestion().equals(request.getAnswer())) {
			throw new RuntimeException("Secret question or answer is incorrect");
		}
		user.setPassword(passwordEncoder.encode(request.getNewPassword()));
		userRepository.save(user);
		return user;
	}

	@Transactional
	public void updatePassword(String userid, PasswordChangeRequest request) {
		User user = userRepository.findById(userid)
				.orElseThrow(() -> new ResourceNotFoundException("No user found with id: " + userid));
		if (user.getSecretQuestion().getId() != request.getSecurityQuestionId()
				|| !user.getAnswerToSecretQuestion().equals(request.getAnswer())) {
			throw new RuntimeException("Secret question or answer is incorrect");
		}
		user.setPassword(passwordEncoder.encode(request.getNewPassword()));
		userRepository.save(user);
	}

	public ValidationDto validateAuthToken(String token) {
		ValidationDto response = new ValidationDto();
		try {
			token = token.substring(7);
			String email = jwtProvider.extractSubject(token);
			User user = userRepository.findByEmail(email)
					.orElseThrow(() -> new ResourceNotFoundException("No user found with email: " + email));
			response.setStatus(true);
			response.setUserId(user.getUserId());
			response.setRole(user.getRole().toString());
		} catch (Exception e) {
			response.setStatus(false);
		}
		return response;
	}

	private String generateUserId() {
		return "U" + UUID.randomUUID().toString();
	}

}
