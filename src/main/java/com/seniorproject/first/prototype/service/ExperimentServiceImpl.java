package com.seniorproject.first.prototype.service;

import com.seniorproject.first.prototype.entity.Experiment;
import com.seniorproject.first.prototype.entity.ParticipantStatus;
import com.seniorproject.first.prototype.entity.Participation;
import com.seniorproject.first.prototype.entity.User;
import com.seniorproject.first.prototype.repository.ExperimentRepository;
import com.seniorproject.first.prototype.repository.ParticipationRepository;
import com.seniorproject.first.prototype.repository.UserRepository;
import com.seniorproject.first.prototype.util.ResponseHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class ExperimentServiceImpl implements ExperimentService{

    private final ExperimentRepository experimentRepository;
    private final UserRepository userRepository;
    private final ParticipationRepository participationRepository;

    @Autowired
    public ExperimentServiceImpl(ExperimentRepository experimentRepository, UserRepository userRepository, ParticipationRepository participationRepository) {
        this.experimentRepository = experimentRepository;
        this.userRepository = userRepository;
        this.participationRepository = participationRepository;
    }


    @Override
    public ResponseEntity<Object> createExperiment(Experiment experiment) {
        if(experiment.getWords() == null)
            return ResponseHandler.generateResponse("Words are not provided", HttpStatus.BAD_REQUEST, null);

        //removing white spaces, converting to lowercase
        for(int i = 0; i < experiment.getWords().size(); i++){
            experiment.getWords().set(i, experiment.getWords().get(i).toLowerCase().replaceAll("\\s+",""));
        }

        experiment.setFrequencyRange(null);
        experiment.setNumberOfWords(experiment.getWords().size());
        experiment.setLengthOfWords(null);
        setOverallResultsAndStatistics(experiment);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        User user = userRepository.findUserByUserEmail(currentUserName).get();
        experiment.setCreator(user);



        experimentRepository.save(experiment);

        return ResponseHandler.generateResponse("Experiment " + experiment.getExperimentId() +" is created", HttpStatus.OK, experiment);

    }

    @Override
    public ResponseEntity<Object> createRandomizedExperiment(Experiment experiment) {
        try {
            log.info(experiment.toString());
            log.info("Creating an experiment with randomized list of words at saveRandomizedExperimentV2()");
            if(experiment.getNumberOfWords() == null)
                return ResponseHandler.generateResponse("Could not create randomized list of words because number of words is not set", HttpStatus.BAD_REQUEST, null);
            experiment.setWords(experimentRepository.findRandomWords(experiment.getNumberOfWords()));
            experiment.setFrequencyRange(null);
            experiment.setLengthOfWords(null);

            setOverallResultsAndStatistics(experiment);

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentUserName = authentication.getName();
            User user = userRepository.findUserByUserEmail(currentUserName).get();
            experiment.setCreator(user);

            experimentRepository.save(experiment);
        } catch (Exception e) {
            log.error("Error at saveRandomizedExperiment(): ", e.getMessage());
            return ResponseHandler.generateResponse("Could not create randomized list of words", HttpStatus.SERVICE_UNAVAILABLE, null);
        }
        log.info("Experiment " + experiment.getExperimentId() + " with randomized list of words is created");
        return ResponseHandler.generateResponse("Experiment " + experiment.getExperimentId() +" with randomized list of words is created", HttpStatus.OK, experiment);

    }

    @Override
    public ResponseEntity<Object> createExperimentLengthAndFrequency(Experiment experiment) {
        if (experiment.getNumberOfWords() == null)
            return ResponseHandler.generateResponse("Number of words is not set", HttpStatus.BAD_REQUEST, null);
        if (experiment.getFrequencyRange() == null || experiment.getLengthOfWords() == null)
            return ResponseHandler.generateResponse("Frequency range or word length is null", HttpStatus.BAD_REQUEST, null);
        if (experiment.getFrequencyRange().size() != 2)
            return ResponseHandler.generateResponse("Frequency range is invalid", HttpStatus.BAD_REQUEST, null);

        Collections.sort(experiment.getFrequencyRange());
        List<String> words = new ArrayList<>();
        switch (experiment.getLengthOfWords()){
            case 3:
                words = experimentRepository.findWordsByFrequencyRangeAndLength3(experiment.getNumberOfWords(),
                        experiment.getFrequencyRange().get(0), experiment.getFrequencyRange().get(1));
                break;
            case 4:
                words = experimentRepository.findWordsByFrequencyRangeAndLength4(experiment.getNumberOfWords(),
                        experiment.getFrequencyRange().get(0), experiment.getFrequencyRange().get(1));
                break;
            case 5:
                words = experimentRepository.findWordsByFrequencyRangeAndLength5(experiment.getNumberOfWords(),
                        experiment.getFrequencyRange().get(0), experiment.getFrequencyRange().get(1));
                break;
            case 6:
                words = experimentRepository.findWordsByFrequencyRangeAndLength6(experiment.getNumberOfWords(),
                        experiment.getFrequencyRange().get(0), experiment.getFrequencyRange().get(1));
                break;
            default:
                return ResponseHandler.generateResponse("Cannot find words with length " + experiment.getLengthOfWords(), HttpStatus.BAD_REQUEST, null);
        }

        experiment.setWords(words);
        setOverallResultsAndStatistics(experiment);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        User user = userRepository.findUserByUserEmail(currentUserName).get();
        experiment.setCreator(user);

        experimentRepository.save(experiment);
        return ResponseHandler.generateResponse("Experiment " + experiment.getExperimentId() +" is created", HttpStatus.OK, experiment);
    }


    //Method that returns all the experiments

    @Override
    public ResponseEntity<Object> getMyExperimentById(Long experimentId) {
        Experiment experiment = experimentRepository.findByExperimentId(experimentId);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(experiment == null || !authentication.getName().equals(experiment.getCreator().getUserEmail()))
            return ResponseHandler.generateResponse("Experiment with "+experimentId+" does not exist or belong to the user", HttpStatus.NOT_FOUND, null);

        return ResponseHandler.generateResponse("Experiment " + experiment.getExperimentId() +" is returned", HttpStatus.OK, experiment);

    }

    @Override
    public ResponseEntity<Object> getExperimentsByEmail(String email) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(email.equals(authentication.getName())){
            return ResponseHandler.generateResponse("Email is your email. Please, navigate to /getMyExperiments to see your created experiments.", HttpStatus.BAD_REQUEST, null);
        }
        Long creatorId = userRepository.findUserByUserEmail(email).get().getUserId();
        List<Experiment> experiments = experimentRepository.findByCreatorUserIdAndIsJoinable(creatorId, Boolean.TRUE);

        List<Experiment> result = new ArrayList<>();

        for(int i = 0; i < experiments.size(); i++){
            Experiment currExp = experiments.get(i);
            if(participationRepository.findParticipationByParticipantUserEmailAndExperiment_ExperimentId(authentication.getName(), currExp.getExperimentId()) != null){
                continue;
            } else {
                // hides user info
                currExp.setCreator(null);
                result.add(currExp);
            }
        }
        return ResponseHandler.generateResponse("Experiments with email " + email , HttpStatus.OK, result);

//        User user = userRepository.findUserByUserEmail(email).get();
//        List<Experiment> experiments = experimentRepository.findAllByCreator(user);
//
//        List<Map<Long, String>> experimentNames;
//
//        for(Experiment experiment : experiments){
//            Map<String, String> name = new HashMap<>();
//            name.put(experiment.getExperimentId(), experiment.getExperimentName());
//
//            experimentNames.add(new Map<String, String>() {
//
//            })
//        }
//
//        return ResponseHandler.generateResponse("Experiment " + experiment.getExperimentId() +" is returned", HttpStatus.OK, experiment);
    }

    @Override
    public ResponseEntity<Object> getAllMyExperiments() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        User user = userRepository.findUserByUserEmail(currentUserName).get();

        List<Experiment> experiments = experimentRepository.findAllByCreator(user);

        return  ResponseHandler.generateResponse("Experiments are returned", HttpStatus.OK, experiments);
    }

    //TODO get all experiments in which the user has not participated and has not sent join request
    @Override
    public ResponseEntity<Object> getAllExperiments() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<Experiment> experiments = experimentRepository.findAll();
        List<Experiment> result = new ArrayList<>();

        for(int i = 0; i < experiments.size(); i++){
            Experiment currExp = experiments.get(i);
            if(currExp.getCreator().getUserEmail().equals(authentication.getName()))
                continue;
          //  participationRepository.findParticipationByExperiment_ExperimentId(currExp.getExperimentId());
            if(participationRepository.findParticipationByParticipantUserEmailAndExperiment_ExperimentId(authentication.getName(), currExp.getExperimentId()) != null)
                continue;

            else{
                // hides user password
                User user = currExp.getCreator();
                user.setPassword(null);
                currExp.setCreator(user);
                result.add(currExp);
            }
        }
        return ResponseHandler.generateResponse("Returning all the available experiments", HttpStatus.OK, result);
    }

    @Override
    public List<Experiment> getMyPendingJoinExperiments() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        List<Participation> participationList = participationRepository.findParticipationsByParticipantUserEmailAndStatus(authentication.getName(), ParticipantStatus.PENDING);

        List<Experiment> pendingExperimentsList = new ArrayList<>();

        for(int i = 0; i < participationList.size(); i++){
            pendingExperimentsList.add(participationList.get(i).getExperiment());
        }

        return pendingExperimentsList;
    }

    @Override
    public List<Experiment> getMyJoinedExperiments() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<Participation> participationList = participationRepository.findParticipationsByParticipantUserEmailAndStatus(authentication.getName(), ParticipantStatus.JOINED);

        List<Experiment> joinedExperimentsList = new ArrayList<>();

        for(int i = 0; i < participationList.size(); i++){
            joinedExperimentsList.add(participationList.get(i).getExperiment());
        }

        return joinedExperimentsList;
    }

    @Override
    public ResponseEntity<Object> getMyTakenExperiments() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<Participation> participationList = participationRepository.findParticipationsByParticipantUserEmailAndStatus(authentication.getName(), ParticipantStatus.TAKEN);
        List<Experiment> takenExperimentsList = new ArrayList<>();

        for(int i = 0; i < participationList.size(); i++){
            takenExperimentsList.add(participationList.get(i).getExperiment());
        }

        return ResponseHandler.generateResponse("Returning the list of taken experiments", HttpStatus.OK, takenExperimentsList);
    }

    @Override
    public ResponseEntity<Object> deleteMyCreatedExperimentById(Long experimentId){
        if(experimentRepository.findByExperimentId(experimentId) == null)
            return ResponseHandler.generateResponse("Experiment " + experimentId +" is not found", HttpStatus.NOT_FOUND, experimentId);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Experiment experiment = experimentRepository.findByExperimentId(experimentId);
        if(!authentication.getName().equals(experiment.getCreator().getUserEmail()))
            return ResponseHandler.generateResponse("Experiment was created by a different user", HttpStatus.FORBIDDEN, experimentId);

        try{
            // find users who participated or requested the experiment and delete the entry
            List<Participation> participations = participationRepository.findParticipationByExperiment_ExperimentId(experimentId);
            User userCreator = userRepository.findUserByUserEmail(experiment.getCreator().getUserEmail()).get();

            for(Participation participation: participations){
                User user = participation.getParticipant();
                log.info("got user ");
                user.getParticipatedExperiments().remove(experiment);
                userRepository.save(user);
                log.info("saved the user");

                //remove user from participation table before deleting the participation entry
                participation.setParticipant(null);
                participation.setExperiment(null);
                participationRepository.save(participation);
                participationRepository.deleteById(participation.getParticipationId());
                //log.info("deleted participation {}", participation);
            }

            //delete entry from creator user
            userCreator.getCreatedExperiments().remove(experiment);
            userRepository.save(userCreator);
            log.info("{}", userCreator.getCreatedExperiments().size());

            experiment.setCreator(null);
            experimentRepository.save(experiment);
            experimentRepository.deleteById(experimentId);
        }
        catch (Exception e){
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);

        }
        return ResponseHandler.generateResponse("Experiment " + experimentId +" is deleted", HttpStatus.OK, experimentId);

    }

    @Override
    public ResponseEntity<Object> updateMyCreatedExperimentById(Long experimentId, Experiment experiment) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Experiment dbExperiment = experimentRepository.findByExperimentId(experimentId);
        if(!authentication.getName().equals(dbExperiment.getCreator().getUserEmail())){
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
        experimentRepository.save(dbExperiment);
        return ResponseHandler.generateResponse("ExperimentInfo was updated successfully", HttpStatus.OK, dbExperiment);
    }

    @Override
    public ResponseEntity<Object> getExperimentStatistics(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        User user = userRepository.findUserByUserEmail(currentUserName).get();
        Experiment experiment = experimentRepository.findByExperimentId(id);

        //TODO who can see statistics?

        Map<String, Object> stat = new HashMap<>();
        stat.put("id", experiment.getExperimentId());
        stat.put("name", experiment.getExperimentName());
        stat.put("averageAge", experiment.getAverageAge());
        stat.put("ParticipantCount", experiment.getParticipantCount());
        stat.put("NumberOfGenderParticipants", experiment.getNumberOfGenderParticipants());
        stat.put("NumberOfDegreeParticipants", experiment.getNumberOfDegreeParticipants());

        return ResponseHandler.generateResponse("Experiment's statistics returned", HttpStatus.OK, stat);

    }

    @Override
    public ResponseEntity<Object> getMyCreatedExperimentByExperimentName(String experimentName) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        User user = userRepository.findUserByUserEmail(currentUserName).get();

        List<Experiment> experiments = experimentRepository.findByExperimentNameIgnoreCaseAndCreator(experimentName, user);

        if(experiments == null)
            return ResponseHandler.generateResponse("Experiments with "+experimentName+" do not exist or belong to the user", HttpStatus.NOT_FOUND, null);

        return ResponseHandler.generateResponse("Experiments are returned", HttpStatus.OK, experiments);

    }



    private void setOverallResultsAndStatistics(Experiment experiment){
        List<Integer> overallResults = new ArrayList<>();
        for(int i = 0; i < experiment.getWords().size(); i++){
            overallResults.add(0);
        }
        experiment.setOverallResults(overallResults);
        experiment.setParticipantCount((long)0);

        List<Integer> temp = new ArrayList<>();
        experiment.setAverageAge((double) 0);
        for(int i=0; i<3; i++){
            temp.add(0);
        }
        experiment.setNumberOfDegreeParticipants(temp);
        experiment.setNumberOfGenderParticipants(temp);
    }

}
