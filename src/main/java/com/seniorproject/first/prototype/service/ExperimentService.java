package com.seniorproject.first.prototype.service;

import com.seniorproject.first.prototype.entity.Experiment;
import org.springframework.stereotype.Service;

@Service
public interface ExperimentService {
    public Experiment createExperiment(Experiment experiment);
}
