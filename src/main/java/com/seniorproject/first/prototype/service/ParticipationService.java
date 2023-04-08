package com.seniorproject.first.prototype.service;

import com.seniorproject.first.prototype.entity.Experiment;
import com.seniorproject.first.prototype.entity.Participation;
import com.seniorproject.first.prototype.model.ExperimentsByEmailRequest;
import com.seniorproject.first.prototype.model.PostParticipateRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public interface ParticipationService {
    public List<Experiment> findExperimentsByEmail(String creatorEmail);

    public Experiment getParticipate(Long experimentId) throws Exception;

    @Transactional
    public Participation postParticipate(PostParticipateRequest postParticipateRequest, Long experimentId) throws Exception;

    public ResponseEntity<Object> postJoin(Long experimentId) throws Exception;

    public List<Participation> getExperimentPendingRequests(Long experimentId) throws Exception;

    public Participation postAcceptJoinRequest(Long participationId) throws Exception;

    public List<Participation> getExperimentJoinedParticipations(Long experimentId) throws Exception;

    public List<Participation> getExperimentTakenParticipations(Long experimentId) throws Exception;

    public Participation postRejectJoinRequest(Long participationId) throws Exception;

    public ResponseEntity<Object> getMyParticipationRequests();

    public List<Participation> getMyTakenParticipations();
}
