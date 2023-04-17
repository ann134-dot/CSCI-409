package com.seniorproject.first.prototype.service;

import com.seniorproject.first.prototype.entity.Experiment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ExperimentService {
    ResponseEntity<Object> createExperiment(Experiment experiment);
    ResponseEntity<Object> createRandomizedExperiment(Experiment experiment);
    ResponseEntity<Object> createExperimentLengthAndFrequency(Experiment experiment);

    ResponseEntity<Object> getMyExperimentById(Long experimentId);
    ResponseEntity<Object> getExperimentsByEmail(String email);

    ResponseEntity<Object> getAllMyExperiments();
    ResponseEntity<Object> getAllExperiments();
    public ResponseEntity<Object> getMyCreatedExperimentByExperimentName(String experimentName);

    public List<Experiment> getMyPendingJoinExperiments();

    public List<Experiment> getMyJoinedExperiments();

    public ResponseEntity<Object> getMyTakenExperiments();

    ResponseEntity<Object> deleteMyCreatedExperimentById(Long experimentId);

    public ResponseEntity<Object> updateMyCreatedExperimentById(Long experimentId, Experiment experiment) throws Exception;


    ResponseEntity<Object> getExperimentStatistics(Long id);
}
