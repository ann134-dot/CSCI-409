package com.seniorproject.first.prototype.service;

import com.seniorproject.first.prototype.entity.Experiment;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ExperimentService {
    public Experiment createExperiment(Experiment experiment);

    public Experiment getMyCreatedExperimentById(Long experimentId);

    public List<Experiment> getMyPendingJoinExperiments();

    public List<Experiment> getMyJoinedExperiments();

    public List<Experiment> getMyTakenExperiments();

    public void deleteMyCreatedExperimentById(Long experimentId) throws Exception;

    public Experiment updateMyCreatedExperimentById(Long experimentId, Experiment experiment) throws Exception;

    public Experiment getMyCreatedExperimentByExperimentName(String experimentName);
}
