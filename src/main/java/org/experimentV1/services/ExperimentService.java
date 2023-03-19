package org.experimentV1.services;

import org.experimentV1.entity.Experiment;
import org.springframework.http.ResponseEntity;

public interface ExperimentService {
    // A user can provide his own list of words
    // Or request a list of randomized words given the number of words
    // Or request to retrieve from the database with the following parameters:
    // 1. Number of words 2.frequency range(between A and B range || less than X || more than X)
    // 3. Length of words(fixed: only 3 or 4 or 5 or 6 || mixed: 3 and 4)
    // 4. Number of seconds to show a word: for storing purposes(handled by the front)

    // Case 1:
    //      provide only frequency range
    //Case 2:
    //      only length of words
    //      Case 2.1:
    //          get words with length equal to N
    //      Case 2.2:
    //          get words with different lengths((3 and 5) or (3 and 4 and 5)).
    //          Case 2.2.1:
    //              provide number of words per length: ex. get 3 words for length 5 and 5 words for length 3
    //          Case 2.2.2:
    //              Randomize number of words for each length
    //Case 3:
    //      frequency and length
    ResponseEntity<Object> createRandomizedExperiment(Experiment experiment);
    ResponseEntity<Object> createExperiment(Experiment experiment);
    ResponseEntity<Object> deleteExperimentById(Long experimentId);
    ResponseEntity<Object> deleteExperimentByUser(String user);
    ResponseEntity<Object> getAllExperiments();
    ResponseEntity<Object> getExperiment(Long experimentId);
    ResponseEntity<Object> getExperimentByUser();
}
