package com.seniorproject.first.prototype.util;

import com.seniorproject.first.prototype.entity.Experiment;
import com.seniorproject.first.prototype.entity.Role;
import com.seniorproject.first.prototype.entity.User;
import com.seniorproject.first.prototype.repository.ExperimentRepository;
import com.seniorproject.first.prototype.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
@Component
@Slf4j
public class DatabaseSeeder implements CommandLineRunner {
    private final ExperimentRepository experimentRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DatabaseSeeder(ExperimentRepository experimentRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.experimentRepository = experimentRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public void run(String... args) throws Exception {
        Experiment experiment;
        try {
            User user = User.builder()
                    .userEmail("test@gmail.com")
                    .age(22L)
                    .gender("m")
                    .degree("UG")
                    .firstName("Test")
                    .lastName("Testov")
                    .role(Role.ADMIN)
                    .password(passwordEncoder.encode("1234"))
                    .build();
            userRepository.save(user);

            User user2 = User.builder()
                    .userEmail("user@gmail.com")
                    .age(22L)
                    .gender("m")
                    .degree("UG")
                    .firstName("User")
                    .lastName("Testov")
                    .role(Role.USER)
                    .password(passwordEncoder.encode("1234"))
                    .build();
            userRepository.save(user2);

            experiment = Experiment.builder()

                    .build();
        } catch (Exception e) {
            log.error("error in creating a user: {}", e.getMessage());
        }


    }
}

