package com.cts.ticketbookingservice.config;

import com.cts.ticketbookingservice.model.Role;
import com.cts.ticketbookingservice.model.SecretQuestion;
import com.cts.ticketbookingservice.model.User;
import com.cts.ticketbookingservice.repository.SecretQuestionRepo;
import com.cts.ticketbookingservice.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class InsertConfig implements CommandLineRunner {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private SecretQuestionRepo secretRepo;

    @Override
    public void run(String... args) throws Exception {
        SecretQuestion question1 = SecretQuestion.builder().id((long)1).question("What is your mother's maiden name?").build();
        SecretQuestion question2 = SecretQuestion.builder().id((long)2).question("What is your pet's name?").build();


        User user1 = User.builder().userId("A346490ab-84c1-4d8b-87b8-92b98337438c")
                .email("jamesKadams@gmail.com").firstName("James").lastName("Adams")
                .password("$2a$10$v76BPGs6I/AJHW9//FIb4efDffWs5p2jPTTYCzwxRbAqLEvFfIR2e")
                .role(Role.ADMIN)
                .secretQuestion(question2)
                .answerToSecretQuestion("Michu").build();


        secretRepo.save(question1);
        secretRepo.save(question2);


        userRepo.save(user1);
        log.info("All The Data Related to user Saved Successfully");
    }
}
