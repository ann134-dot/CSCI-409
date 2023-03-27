package com.seniorproject.first.prototype.service;

import com.seniorproject.first.prototype.entity.Experiment;
import com.seniorproject.first.prototype.entity.Participation;
import com.seniorproject.first.prototype.entity.User;
import com.seniorproject.first.prototype.repository.ExperimentRepository;
import com.seniorproject.first.prototype.repository.ParticipationRepository;
import com.seniorproject.first.prototype.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ExperimentServiceImpl implements ExperimentService{

    @Autowired
    private ExperimentRepository experimentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ParticipationRepository participationRepository;
    @Override
    public Experiment createExperiment(Experiment experiment) {

        List<Integer> overallResults = new ArrayList<>();
        for(int i = 0; i < experiment.getWords().size(); i++){
            overallResults.add(0);
        }
        experiment.setOverallResults(overallResults);
        experiment.setParticipantCount((long)0);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        User user = userRepository.findUserByUserEmail(currentUserName).get();
        experiment.setCreator(user);

        return experimentRepository.save(experiment);
    }

    @Override
    public Experiment getMyCreatedExperimentById(Long experimentId) {
        return experimentRepository.findByExperimentId(experimentId);
    }

    @Override
    public List<Experiment> getMyPendingJoinExperiments() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        List<Participation> participationList = participationRepository.findParticipationsByParticipantUserEmailAndStatus(authentication.getName(), "pending");

        List<Experiment> pendingExperimentsList = new ArrayList<>();

        for(int i = 0; i < participationList.size(); i++){
            pendingExperimentsList.add(participationList.get(i).getExperiment());
        }

        return pendingExperimentsList;
    }

    @Override
    public List<Experiment> getMyJoinedExperiments() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<Participation> participationList = participationRepository.findParticipationsByParticipantUserEmailAndStatus(authentication.getName(), "joined");

        List<Experiment> joinedExperimentsList = new ArrayList<>();

        for(int i = 0; i < participationList.size(); i++){
            joinedExperimentsList.add(participationList.get(i).getExperiment());
        }

        return joinedExperimentsList;
    }

    @Override
    public List<Experiment> getMyTakenExperiments() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<Participation> participationList = participationRepository.findParticipationsByParticipantUserEmailAndStatus(authentication.getName(), "taken");
        List<Experiment> takenExperimentsList = new ArrayList<>();

        for(int i = 0; i < participationList.size(); i++){
            takenExperimentsList.add(participationList.get(i).getExperiment());
        }

        return takenExperimentsList;
    }

    @Override
    public void deleteMyCreatedExperimentById(Long experimentId) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Experiment experiment = experimentRepository.findByExperimentId(experimentId);
        if(!authentication.getName().equals(experiment.getCreator().getUserEmail())){
            throw new Exception("Experiment was created by a different user");
        }
        else {
            experimentRepository.deleteById(experimentId);
        }
    }

    @Override
    public Experiment updateMyCreatedExperimentById(Long experimentId, Experiment experiment) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Experiment dbExperiment = experimentRepository.findByExperimentId(experimentId);
        if(!authentication.getName().equals(experiment.getCreator().getUserEmail())){
            throw new Exception("Experiment was created by a different user");
        }
        else {
            //Update name
            if(Objects.nonNull(experiment.getExperimentName()) && !"".equalsIgnoreCase(experiment.getExperimentName())){
                dbExperiment.setExperimentName(experiment.getExperimentName());
            }
            //update description
            if(Objects.nonNull(experiment.getDescription()) && !"".equalsIgnoreCase(experiment.getDescription())){
                dbExperiment.setDescription(experiment.getDescription());
            }
            //toggle isJoinable
            if(experiment.getIsJoinable() != dbExperiment.getIsJoinable()){
                dbExperiment.setIsJoinable(!dbExperiment.getIsJoinable());
            }
        }
        return experimentRepository.save(dbExperiment);
    }

    //to be implemented
    @Override
    public Experiment getMyCreatedExperimentByExperimentName(String experimentName) {
        return experimentRepository.findByExperimentNameIgnoreCase(experimentName);
    }

}
