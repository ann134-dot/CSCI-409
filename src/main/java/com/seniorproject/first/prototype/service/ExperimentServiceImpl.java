package com.seniorproject.first.prototype.service;

import com.seniorproject.first.prototype.entity.Experiment;
import com.seniorproject.first.prototype.entity.User;
import com.seniorproject.first.prototype.repository.ExperimentRepository;
import com.seniorproject.first.prototype.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExperimentServiceImpl implements ExperimentService{

    @Autowired
    private ExperimentRepository experimentRepository;
    @Autowired
    private UserRepository userRepository;
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

}
