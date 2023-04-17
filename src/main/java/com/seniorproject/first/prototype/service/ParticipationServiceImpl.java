package com.seniorproject.first.prototype.service;

import com.seniorproject.first.prototype.entity.Experiment;
import com.seniorproject.first.prototype.entity.ParticipantStatus;
import com.seniorproject.first.prototype.entity.Participation;
import com.seniorproject.first.prototype.entity.User;
import com.seniorproject.first.prototype.model.PostParticipateRequest;
import com.seniorproject.first.prototype.repository.ExperimentRepository;
import com.seniorproject.first.prototype.repository.ParticipationRepository;
import com.seniorproject.first.prototype.repository.UserRepository;
import com.seniorproject.first.prototype.util.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Object> getParticipate(Long experimentId) throws Exception {
        Experiment experiment = experimentRepository.findByExperimentId(experimentId);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(experiment.getCreator().getUserEmail().equals(authentication.getName())){
            throw new Exception("Can not participate in your own experiments");
        }
        if(participationRepository.findParticipationByParticipantUserEmailAndExperiment_ExperimentIdAndStatus(authentication.getName(), experimentId, ParticipantStatus.JOINED) == null){
            throw new Exception("Your join request was not sent OR was rejected OR was not approved yet");
        }
        if(participationRepository.findParticipationByParticipantUserEmailAndExperiment_ExperimentIdAndStatus(authentication.getName(), experimentId, ParticipantStatus.TAKEN) != null){
            throw new Exception("You have already taken this experiment");
        }
        return ResponseHandler.generateResponse("Returning the experiment data for participation", HttpStatus.OK, experimentRepository.findByExperimentId(experimentId));
    }

    @Override
    @Transactional
    public ResponseEntity<Object> postParticipate(PostParticipateRequest postParticipateRequest, Long experimentId) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Experiment experiment = experimentRepository.findByExperimentId(experimentId);
        Participation participation = participationRepository.findParticipationByParticipantUserEmailAndExperiment_ExperimentIdAndStatus(authentication.getName(), experimentId, ParticipantStatus.JOINED);
        if(participation == null){
            throw new Exception("Users that did not join the experiment can not take it");
        }
        if(participationRepository.findParticipationByParticipantUserEmailAndExperiment_ExperimentIdAndStatus(authentication.getName(), experimentId, ParticipantStatus.TAKEN) != null){
            throw new Exception("You have already taken the experiment");
        }

        //removing white spaces, converting to lowercase
        for(int i = 0; i < postParticipateRequest.getParticipantResponseList().size(); i++){
            postParticipateRequest.getParticipantResponseList().set(i, postParticipateRequest.getParticipantResponseList().get(i).toLowerCase().replaceAll("\\s+",""));
        }

        for(int i = 0; i < experiment.getWords().size(); i++){
            if(postParticipateRequest.getParticipantResponseList().contains(experiment.getWords().get(i))){
                experiment.getOverallResults().set(i, experiment.getOverallResults().get(i) + 1);
                participation.getParticipantResults().set(i, participation.getParticipantResults().get(i) + 1);
            }
        }


        //experiment.getParticipations().add(participation);

        experiment.setAverageAge(
                (experiment.getAverageAge()*experiment.getParticipantCount() + participation.getParticipant().getAge()) /
                        (experiment.getParticipantCount() + 1)
        );
        experiment.setParticipantCount(experiment.getParticipantCount() + 1);

        // TODO change to enum
        switch (participation.getParticipant().getGender().toLowerCase()){
            case "male":
                experiment.getNumberOfGenderParticipants().set(0,
                        experiment.getNumberOfGenderParticipants().get(0)+1
                );
            case "female":
                experiment.getNumberOfGenderParticipants().set(1,
                        experiment.getNumberOfGenderParticipants().get(1)+1);
            default:
                experiment.getNumberOfGenderParticipants().set(2,
                        experiment.getNumberOfGenderParticipants().get(2)+1);
        }

        //TODO change to enum and add other options
        switch (participation.getParticipant().getDegree().toLowerCase()){
            case "bachelor":
                experiment.getNumberOfDegreeParticipants().set(0,
                        experiment.getNumberOfDegreeParticipants().get(0)+1
                );
            case "master":
                experiment.getNumberOfDegreeParticipants().set(1,
                        experiment.getNumberOfDegreeParticipants().get(1)+1);
            default:
                experiment.getNumberOfDegreeParticipants().set(2,
                        experiment.getNumberOfDegreeParticipants().get(2)+1);
        }


        experimentRepository.save(experiment);

        participation.setExperiment(experiment);
        participation.setStatus(ParticipantStatus.TAKEN);
        participationRepository.save(participation);
        return ResponseHandler.generateResponse("Participation successful, returning back the results", HttpStatus.OK, participation);
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
    public ResponseEntity<Object> getExperimentPendingRequests(Long experimentId) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Experiment experiment = experimentRepository.findByExperimentId(experimentId);
        if(!authentication.getName().equals(experiment.getCreator().getUserEmail())){
            return ResponseHandler.generateResponse("Can not access someone else's experiment", HttpStatus.BAD_REQUEST, null);
        }
        List<Participation> participations;

        try{
            participations = participationRepository.findParticipationsByExperimentExperimentIdAndStatus(experimentId, ParticipantStatus.PENDING);
        }
        catch (Exception e){
            return ResponseHandler.generateResponse("DB exception:", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

        return ResponseHandler.generateResponse("returned", HttpStatus.OK, participations);

    }

    @Override
    public ResponseEntity<Object> postAcceptJoinRequest(Long participationId) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Participation participation = participationRepository.findById(participationId).get();

        Experiment experiment = experimentRepository.findByExperimentId(participation.getExperiment().getExperimentId());
        if(!authentication.getName().equals(experiment.getCreator().getUserEmail())){
            return ResponseHandler.generateResponse("Can not access someone else's experiment", HttpStatus.BAD_REQUEST, null);

        }
        Participation participation1;
        participation.setStatus(ParticipantStatus.JOINED);
        try{

            participation1 = participationRepository.save(participation);
        }
        catch (Exception e){
            return ResponseHandler.generateResponse("DB exception:", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

        return ResponseHandler.generateResponse("returned", HttpStatus.OK, participation1);

    }

    @Override
    public ResponseEntity<Object> postRejectJoinRequest(Long participationId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Participation participation = participationRepository.findById(participationId).get();

        Experiment experiment = experimentRepository.findByExperimentId(participation.getExperiment().getExperimentId());
        if(!authentication.getName().equals(experiment.getCreator().getUserEmail())){
            return ResponseHandler.generateResponse("Can not access someone else's experiment", HttpStatus.BAD_REQUEST, null);
        }

        Participation participation1;
        participation.setStatus(ParticipantStatus.REJECTED);
        try{

            participation1 = participationRepository.save(participation);
        }
        catch (Exception e){
            return ResponseHandler.generateResponse("DB exception:", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

        return ResponseHandler.generateResponse("returned", HttpStatus.OK, participation1);
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
    public ResponseEntity<Object> getMyTakenParticipation(Long experimentId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Participation result = participationRepository.findParticipationByParticipantUserEmailAndExperiment_ExperimentIdAndStatus(authentication.getName(), experimentId, ParticipantStatus.TAKEN);

        return ResponseHandler.generateResponse("Returned results for the taken experiment", HttpStatus.OK, result);
    }

    @Override
    public List<Participation> getExperimentJoinedParticipations(Long experimentId) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!authentication.getName().equals(experimentRepository.findByExperimentId(experimentId).getCreator().getUserEmail())){
            throw new Exception("Not permitted");
        } else {
            return participationRepository.findParticipationsByExperimentExperimentIdAndStatus(experimentId, ParticipantStatus.JOINED);
        }
    }

    @Override
    public ResponseEntity<Object> getExperimentTakenParticipations(Long experimentId) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!authentication.getName().equals(experimentRepository.findByExperimentId(experimentId).getCreator().getUserEmail())){
            throw new Exception("Not permitted");
        } else {
            List<Participation> result = participationRepository.findParticipationsByExperimentExperimentIdAndStatus(experimentId, ParticipantStatus.TAKEN);
            return ResponseHandler.generateResponse("Returned taken participations for your experiment", HttpStatus.OK, result);
        }
    }
}
