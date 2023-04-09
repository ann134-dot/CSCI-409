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

import java.util.List;

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
            final User user = User.builder()
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

            final User user2 = User.builder()
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

            List<String> words = experimentRepository.findRandomWords(4);
            experiment = Experiment.builder()
                    .experimentName("EDXP1")
                    .description("description of EXP3")
                    .isJoinable(true)
                    .wordTime(1.0)
                    .betweenWordTime(1.5)
                    .numberOfWords(4)
                    .words(words)
                    .build();
            experiment.setCreator(userRepository.findUserByUserEmail("user@gmail.com").get());
            experimentRepository.save(experiment);
            Experiment experiment2 = Experiment.builder()
                    .experimentName("ED3")
                    .description("description of EXP3")
                    .isJoinable(true)
                    .wordTime(1.0)
                    .betweenWordTime(1.5)
                    .numberOfWords(4)
                    .words(words)
                    .build();
            experiment.setCreator(userRepository.findUserByUserEmail("test@gmail.com").get());
            experimentRepository.save(experiment2);
        } catch (Exception e) {
            log.error("error: {}", e.getMessage());
        }


    }
}

