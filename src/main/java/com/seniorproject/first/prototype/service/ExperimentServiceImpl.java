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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        Map<Integer, Integer> overallResults = new HashMap<>();
        for(int i = 0; i < experiment.getWords().size(); i++){
            overallResults.put(i, 0);
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

}
