package org.experimentV1;

import lombok.extern.slf4j.Slf4j;
import org.experimentV1.entity.Experiment;
import org.experimentV1.repositories.ExperimentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
public class DatabaseSeeder implements CommandLineRunner {
    private final ExperimentRepository experimentRepository;

    @Autowired
    public DatabaseSeeder(ExperimentRepository experimentRepository) {
        this.experimentRepository = experimentRepository;
    }


    @Override
    public void run(String... args) throws Exception {
        Experiment experiment;
        try {
            experiment = Experiment.builder()
                    .name("Experiment1")
                    .description("description of the first experiment")
                    .words(Arrays.asList("word1", "word2", "word3", "word4")) // test cases are manually inserted
                   // .frequencyOfWords(10000)
                    .lengthOfWords(List.of(5))
                    .numberOfSecondsPerWord(2.0)
                    .numberOfWords(4)
                    .build();
            experimentRepository.save(experiment);
        } catch (Exception e) {
            log.error("error in creating an experiment: {}", e.getMessage());
        }


    }
}
