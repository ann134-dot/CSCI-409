package com.seniorproject.first.prototype.service;

import com.seniorproject.first.prototype.entity.Experiment;
import com.seniorproject.first.prototype.entity.Participation;
import com.seniorproject.first.prototype.model.ExperimentsByEmailRequest;
import com.seniorproject.first.prototype.model.PostParticipateRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public interface ParticipationService {
    public List<Experiment> findExperimentsByEmail(ExperimentsByEmailRequest experimentsByEmailRequest);

    public Experiment getParticipate(Long experimentId) throws Exception;

    @Transactional
    public Participation postParticipate(PostParticipateRequest postParticipateRequest, Long experimentId) throws Exception;
}
