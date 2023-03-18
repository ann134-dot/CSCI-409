package org.experimentV1.services;

import lombok.extern.slf4j.Slf4j;
import org.experimentV1.entity.Experiment;
import org.experimentV1.repositories.ExperimentRepository;
import org.experimentV1.util.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Stream;

@Service
@Slf4j
public class ExperimentServiceImpl implements ExperimentService{
    private final ExperimentRepository experimentRepository;

    @Autowired
    public ExperimentServiceImpl(ExperimentRepository experimentRepository) {
        this.experimentRepository = experimentRepository;
    }

    public ResponseEntity<Object> saveRandomizedExperimentV1(String name, String description, Double numberOfSecondsPerWord, Integer numberOfWords) {
        Experiment experiment;
        try {
            log.info("Creating an experiment with randomized list of words");
            experiment = Experiment.builder()
                    .name(name)
                    .description(description)
                    .numberOfSecondsPerWord(numberOfSecondsPerWord)
                    .numberOfWords(numberOfWords)
                    .words(experimentRepository.findRandomWords(numberOfWords))
                    .build();
            experimentRepository.save(experiment);

        } catch (Exception e) {
            log.error("Error at saveRandomizedExperiment(): ", e.getMessage());
            return ResponseHandler.generateResponse("Could not create randomized list of words", HttpStatus.SERVICE_UNAVAILABLE, null);
        }
        log.info("Experiment " + experiment.getExperimentId() + "with randomized list of words is created");
        return ResponseHandler.generateResponse("Experiment " + experiment.getExperimentId() +" with randomized list of words is created", HttpStatus.OK, experiment);

    }

    public ResponseEntity<Object> saveRandomizedExperimentV2(Experiment experiment) {
        try {
            log.info(experiment.toString());
            log.info("Creating an experiment with randomized list of words at saveRandomizedExperimentV2()");
            experiment.setWords(experimentRepository.findRandomWords(experiment.getNumberOfWords()));
            experiment.setFrequencyRange(null);
            experiment.setLengthOfWords(null);
            experimentRepository.save(experiment);
        } catch (Exception e) {
            log.error("Error at saveRandomizedExperiment(): ", e.getMessage());
            return ResponseHandler.generateResponse("Could not create randomized list of words", HttpStatus.SERVICE_UNAVAILABLE, null);
        }
        log.info("Experiment " + experiment.getExperimentId() + " with randomized list of words is created");
        return ResponseHandler.generateResponse("Experiment " + experiment.getExperimentId() +" with randomized list of words is created", HttpStatus.OK, experiment);

    }

    @Override
    public ResponseEntity<Object> createRandomizedExperiment(Experiment experiment) {
        return saveRandomizedExperimentV2(experiment);
    }

    @Override
    public ResponseEntity<Object> createExperiment(Experiment experiment) {
        if (experiment.getFrequencyRange() == null || experiment.getLengthOfWords() == null)
            return ResponseHandler.generateResponse("Frequency range or word length is null", HttpStatus.BAD_REQUEST, null);
        Collections.sort(experiment.getFrequencyRange());
        experiment.setWords(
                experimentRepository.findWordsByFrequencyRangeAndLength3(experiment.getNumberOfWords(),
                        experiment.getFrequencyRange().get(0), experiment.getFrequencyRange().get(1))
        );
        experimentRepository.save(experiment);
        return ResponseHandler.generateResponse("Experiment " + experiment.getExperimentId() +" is created", HttpStatus.OK, experiment);

    }

    public ResponseEntity<Object> createExperimentWithFrequencyRange(Experiment experiment) {
        if(experiment.getFrequencyRange() == null || experiment.getFrequencyRange().size() != 2)
            return ResponseHandler.generateResponse("Frequency range must contain two values", HttpStatus.BAD_REQUEST, null);
        Collections.sort(experiment.getFrequencyRange());
        experiment.setWords(experimentRepository.findWordsByFrequency(experiment.getNumberOfWords(), experiment.getFrequencyRange().get(0), experiment.getFrequencyRange().get(1)));
        experiment.setLengthOfWords(null);
        experimentRepository.save(experiment);
        return ResponseHandler.generateResponse("Experiment " + experiment.getExperimentId() +" is created", HttpStatus.OK, experiment);
    }

    public ResponseEntity<Object> createExperimentWithLength(Experiment experiment, List<Integer> numberOfWordsPerLength ) {
        if(numberOfWordsPerLength.size() != experiment.getLengthOfWords().size())
            return ResponseHandler.generateResponse("numberOfWordsPerLength.size() != experiment.getLengthOfWords().size(): cannot match, Invalid request", HttpStatus.BAD_REQUEST, null);

        List<String> words = new ArrayList<>();
        for(int s = 0; s<numberOfWordsPerLength.size(); s++){
            words = Stream.concat(words.stream(),
                    experimentRepository.findWordsByLength(numberOfWordsPerLength.get(s))
                            //experimentRepository.findWordsByLength(numberOfWordsPerLength.get(s), experiment.getLengthOfWords().get(s))
                            .stream()
            ).toList();
        }
        experiment.setWords(words);
        // Front should sum the words
        experiment.setNumberOfWords(numberOfWordsPerLength.stream()
                .reduce(0, Integer::sum));
        experiment.setFrequencyRange(null);
        experimentRepository.save(experiment);
        return ResponseHandler.generateResponse("Experiment " + experiment.getExperimentId() +" is created", HttpStatus.OK, experiment);
    }

    public ResponseEntity<Object> createExperimentWithLengthAndFrequency(Experiment experiment, List<Integer> numberOfWordsPerLength ) {
        if(experiment.getFrequencyRange().size() != 2 && !experiment.getFrequencyRange().isEmpty())
            return ResponseHandler.generateResponse("Frequency range must contain two values", HttpStatus.BAD_REQUEST, null);

        if(numberOfWordsPerLength.size() != experiment.getLengthOfWords().size())
            return ResponseHandler.generateResponse("numberOfWordsPerLength.size() != experiment.getLengthOfWords().size(): cannot match, Invalid request", HttpStatus.BAD_REQUEST, null);

        List<Integer> range = experiment.getFrequencyRange();
        Collections.sort(range);
        List<String> words = new ArrayList<>();
        for(int s = 0; s<numberOfWordsPerLength.size(); s++){
            words = Stream.concat(words.stream(),
                    experimentRepository.findWordsByLengthAndFrequency(numberOfWordsPerLength.get(s), range.get(0), range.get(1))
                            //experimentRepository.findWordsByLength(numberOfWordsPerLength.get(s), experiment.getLengthOfWords().get(s))
                            .stream()
            ).toList();
        }
        experiment.setWords(words);
        // Front should sum the words
        experiment.setNumberOfWords(numberOfWordsPerLength.stream()
                .reduce(0, Integer::sum));
        experimentRepository.save(experiment);
        return ResponseHandler.generateResponse("Experiment " + experiment.getExperimentId() +" is created", HttpStatus.OK, experiment);
    }


    //    Add Logging, change native query to solve the dynamic table naming issue
    // Case 1:
    //      provide only frequency range
    //Case 2:
    //      only length of words
    //      Case 2.1:
    //          get words with length equal to N
    //      Case 2.2:
    //          get words with different lengths((3 and 5) or (3 and 4 and 5)).
    //Case 3:
    //      frequency and length
    public ResponseEntity<Object> createExperimentV1(Experiment experiment, List<Integer> numberOfWordsPerLength) {

        return ResponseHandler.generateResponse("Experiment " + experiment.getExperimentId() +" is created", HttpStatus.NOT_FOUND, experiment);
    }


    @Override
    public ResponseEntity<Object> deleteExperiment(Integer experimentId) {
        return null;
    }

    @Override
    public ResponseEntity<Object> getAllExperiments() {
        return null;
    }

    @Override
    public ResponseEntity<Object> getExperiment(Integer experimentId) {
        return null;
    }

    @Override
    public ResponseEntity<Object> getExperimentByUser() {
        return null;
    }


}
