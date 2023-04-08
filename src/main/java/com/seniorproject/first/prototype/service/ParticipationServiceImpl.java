package com.seniorproject.first.prototype.service;

import com.seniorproject.first.prototype.entity.Experiment;
import com.seniorproject.first.prototype.entity.ParticipantStatus;
import com.seniorproject.first.prototype.entity.Participation;
import com.seniorproject.first.prototype.entity.User;
import com.seniorproject.first.prototype.model.ExperimentsByEmailRequest;
import com.seniorproject.first.prototype.model.PostParticipateRequest;
import com.seniorproject.first.prototype.repository.ExperimentRepository;
import com.seniorproject.first.prototype.repository.ParticipationRepository;
import com.seniorproject.first.prototype.repository.UserRepository;
import com.seniorproject.first.prototype.util.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class ParticipationServiceImpl implements ParticipationService{
    @Autowired
    private ParticipationRepository participationRepository;
    @Autowired
    private ExperimentRepository experimentRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Experiment> findExperimentsByEmail(String creatorEmail) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long creatorId = userRepository.findUserByUserEmail(creatorEmail).get().getUserId();
        List<Experiment> experiments = experimentRepository.findByCreatorUserIdAndIsJoinable(creatorId, Boolean.TRUE);

        List<Experiment> result = new ArrayList<>();

        for(int i = 0; i < experiments.size(); i++){
            Experiment currExp = experiments.get(i);
            if(currExp.getCreator().getUserEmail().equals(authentication.getName())){
                continue;
            }
            if(participationRepository.findParticipationByParticipantUserEmailAndExperiment_ExperimentId(authentication.getName(), currExp.getExperimentId()) != null){
                continue;
            } else {
                result.add(currExp);
            }
        }

        return result;
    }

    @Override
    public Experiment getParticipate(Long experimentId) throws Exception {
        Experiment experiment = experimentRepository.findByExperimentId(experimentId);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(experiment.getCreator().getUserEmail().equals(authentication.getName())){
            throw new Exception("Can not participate in your own experiments");
        }
        if(experiment.getIsJoinable() == false){
            throw new Exception("Experiment is not joinable");
        }
        if(participationRepository.findParticipationByParticipantUserEmailAndExperiment_ExperimentIdAndStatus(authentication.getName(), experimentId, "taken") != null){
            throw new Exception("You have already taken this experiment");
        }
        if(participationRepository.findParticipationByParticipantUserEmailAndExperiment_ExperimentIdAndStatus(authentication.getName(), experimentId, "joined") != null){
            throw new Exception("You have already joined this experiment");
        }
        if(participationRepository.findParticipationByParticipantUserEmailAndExperiment_ExperimentIdAndStatus(authentication.getName(), experimentId, "pending") != null){
            throw new Exception("Your request to join was already sent");
        }
        return experimentRepository.findByExperimentId(experimentId);
    }

    @Override
    @Transactional
    public Participation postParticipate(PostParticipateRequest postParticipateRequest, Long experimentId) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Experiment experiment = experimentRepository.findByExperimentId(experimentId);
        Participation participation = participationRepository.findParticipationByParticipantUserEmailAndExperiment_ExperimentIdAndStatus(authentication.getName(), experimentId, "joined");
        if(participation == null){
            throw new Exception("Users that did not join the experiment can not take it");
        }
        if(participationRepository.findParticipationByParticipantUserEmailAndExperiment_ExperimentIdAndStatus(authentication.getName(), experimentId, "taken") != null){
            throw new Exception("You have already taken the experiment");
        }

        for(int i = 0; i < experiment.getWords().size(); i++){
            if(postParticipateRequest.getParticipantResponseList().contains(experiment.getWords().get(i))){
                experiment.getOverallResults().set(i, experiment.getOverallResults().get(i) + 1);
                participation.getParticipantResults().set(i, participation.getParticipantResults().get(i) + 1);
            }
        }

        experiment.setParticipantCount(experiment.getParticipantCount() + 1);
        //experiment.getParticipations().add(participation);
        experimentRepository.save(experiment);

        participation.setExperiment(experiment);
        participation.setStatus(ParticipantStatus.TAKEN);

        return participationRepository.save(participation);
    }

    @Override
    public ResponseEntity<Object> postJoin(Long experimentId) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Experiment experiment = experimentRepository.findByExperimentId(experimentId);
        if(experiment.getCreator().getUserEmail().equals(authentication.getName())){
            throw new Exception("Can not join your own experiment");
        }
        if(participationRepository.findParticipationByParticipantUserEmailAndExperiment_ExperimentId(authentication.getName(), experimentId) != null){
            throw new Exception("Request was already sent OR Already joined OR Experiment was already taken");
        }

        Participation participation = new Participation();
        User participant = userRepository.findUserByUserEmail(authentication.getName()).get();
        participation.setParticipant(participant);
        participation.setStatus(ParticipantStatus.PENDING);
        participation.setExperiment(experimentRepository.findByExperimentId(experimentId));

        List<Integer> participantResults = new ArrayList<>();
        for(int i = 0; i < experiment.getWords().size(); i++){
            participantResults.add(0);
        }
        participation.setParticipantResults(participantResults);
        participationRepository.save(participation);

        return ResponseHandler.generateResponse("The join request was successfully sent", HttpStatus.OK, participation);
    }

    @Override
    public List<Participation> getExperimentPendingRequests(Long experimentId) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Experiment experiment = experimentRepository.findByExperimentId(experimentId);
        if(!authentication.getName().equals(experiment.getCreator().getUserEmail())){
            throw new Exception("Can not access someone else's experiment");
        }
        return participationRepository.findParticipationsByExperimentExperimentIdAndStatus(experimentId, "pending");
    }

    @Override
    public Participation postAcceptJoinRequest(Long participationId) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Participation participation = participationRepository.findById(participationId).get();

        Experiment experiment = experimentRepository.findByExperimentId(participation.getExperiment().getExperimentId());
        if(!authentication.getName().equals(experiment.getCreator().getUserEmail())){
            throw new Exception("Can not access someone else's experiment");
        }

        participation.setStatus(ParticipantStatus.JOINED);
        return participationRepository.save(participation);
    }

    @Override
    public Participation postRejectJoinRequest(Long participationId) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Participation participation = participationRepository.findById(participationId).get();

        Experiment experiment = experimentRepository.findByExperimentId(participation.getExperiment().getExperimentId());
        if(!authentication.getName().equals(experiment.getCreator().getUserEmail())){
            throw new Exception("Can not access someone else's experiment");
        }

        participation.setStatus(ParticipantStatus.REJECTED);
        return participationRepository.save(participation);
    }

    @Override
    public ResponseEntity<Object> getMyParticipationRequests() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        List<Participation> userParticipationRequests = new ArrayList<>();
        List<Participation> pendingRequests = participationRepository.findParticipationsByParticipantUserEmailAndStatus(authentication.getName(), ParticipantStatus.PENDING);
        List<Participation> acceptedRequests = participationRepository.findParticipationsByParticipantUserEmailAndStatus(authentication.getName(), ParticipantStatus.JOINED);
        List<Participation> rejectedRequests = participationRepository.findParticipationsByParticipantUserEmailAndStatus(authentication.getName(), ParticipantStatus.REJECTED);

        userParticipationRequests.addAll(acceptedRequests);
        userParticipationRequests.addAll(pendingRequests);
        userParticipationRequests.addAll(rejectedRequests);

        return ResponseHandler.generateResponse("Returned the participation requests that the user has sent", HttpStatus.OK, userParticipationRequests);
    }

    @Override
    public List<Participation> getMyTakenParticipations() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return participationRepository.findParticipationsByParticipantUserEmailAndStatus(authentication.getName(), ParticipantStatus.TAKEN);
    }

    @Override
    public List<Participation> getExperimentJoinedParticipations(Long experimentId) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!authentication.getName().equals(experimentRepository.findByExperimentId(experimentId).getCreator().getUserEmail())){
            throw new Exception("Not permitted");
        } else {
            return participationRepository.findParticipationsByExperimentExperimentIdAndStatus(experimentId, "joined");
        }
    }

    @Override
    public List<Participation> getExperimentTakenParticipations(Long experimentId) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!authentication.getName().equals(experimentRepository.findByExperimentId(experimentId).getCreator().getUserEmail())){
            throw new Exception("Not permitted");
        } else {
            return participationRepository.findParticipationsByExperimentExperimentIdAndStatus(experimentId, "taken");
        }
    }
}
