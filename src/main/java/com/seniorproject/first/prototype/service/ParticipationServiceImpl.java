package com.seniorproject.first.prototype.service;

import com.seniorproject.first.prototype.entity.Experiment;
import com.seniorproject.first.prototype.entity.Participation;
import com.seniorproject.first.prototype.model.ExperimentsByEmailRequest;
import com.seniorproject.first.prototype.model.PostParticipateRequest;
import com.seniorproject.first.prototype.repository.ExperimentRepository;
import com.seniorproject.first.prototype.repository.ParticipationRepository;
import com.seniorproject.first.prototype.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
    public List<Experiment> findExperimentsByEmail(ExperimentsByEmailRequest experimentsByEmailRequest) {
        Long creatorId = userRepository.findUserByUserEmail(experimentsByEmailRequest.getEmail()).get().getUserId();
        List<Experiment> experiments = experimentRepository.findByCreatorUserIdAndIsPublic(creatorId, Boolean.TRUE);
        return experiments;
    }

    @Override
    public Experiment getParticipate(Long experimentId) throws Exception {
        Experiment experiment = experimentRepository.findByExperimentId(experimentId);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && experiment.getCreator().getUserEmail().equals(authentication.getName())){
            throw new Exception("Can not participate in your own experiments");
        }
        if(experiment.getIsPublic() == false){
            throw new Exception("Experiment is not public");
        }
        return experimentRepository.findByExperimentId(experimentId);
    }

    @Override
    @Transactional
    public Participation postParticipate(PostParticipateRequest postParticipateRequest, Long experimentId) throws Exception {
        Experiment experiment = experimentRepository.findByExperimentId(experimentId);
        if(experiment.getIsPublic() == false){
            throw new Exception("Not public");
        }
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < experiment.getWords().size(); i++){
            if(postParticipateRequest.getParticipantResponse().containsValue(experiment.getWords().get(i))){
                experiment.getOverallResults().put(i, experiment.getOverallResults().get(i) + 1);
                sb.append("1");
            } else{
                sb.append("0");
            }
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.isAuthenticated()){
            if(authentication.getName().equals(experiment.getCreator().getUserEmail())){
                throw new Exception("Can not participate in your own Experiment");
            } else{
                Participation participation = new Participation();
                participation.setParticipant(userRepository.findUserByUserEmail(authentication.getName()).get());
                participation.setParticipantResults(sb.reverse().toString());
                participation.setExperiment(experiment);

                experiment.setParticipantCount(experiment.getParticipantCount() + 1);
                //experiment.getParticipations().add(participation);

                experimentRepository.save(experiment);

                return participationRepository.save(participation);
            }
        } else {
            Participation participation = new Participation();
            participation.setParticipantResults(sb.reverse().toString());
            participation.setExperiment(experiment);

            experiment.setParticipantCount(experiment.getParticipantCount() + 1);
            //experiment.getParticipations().add(participation);

            experimentRepository.save(experiment);

            return participationRepository.save(participation);
        }
    }
}
