package com.seniorproject.first.prototype.controller;

import com.seniorproject.first.prototype.entity.Experiment;
import com.seniorproject.first.prototype.entity.Participation;
import com.seniorproject.first.prototype.model.ExperimentsByEmailRequest;
import com.seniorproject.first.prototype.model.PostParticipateRequest;
import com.seniorproject.first.prototype.service.ParticipationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ParticipationController {

    @Autowired
    private ParticipationService participationService;

    @GetMapping("/experiments-byEmail")
    public List<Experiment> findExperimentsByEmail(@RequestBody ExperimentsByEmailRequest experimentsByEmailRequest){
        return participationService.findExperimentsByEmail(experimentsByEmailRequest);
    }

    @GetMapping("/experiments-byEmail/participate/{id}")
    public Experiment getParticpate(@PathVariable("id") Long experimentId) throws Exception {
        return participationService.getParticipate(experimentId);
    }

    //joining the experiment
    @PostMapping("experiments-byEmail/join/{id}")
    public Participation postJoin(@PathVariable("id") Long experimentId) throws Exception {
        return participationService.postJoin(experimentId);
    }
    @GetMapping("myCreatedExperiments/pending-requests/{id}")
    public List<Participation> getExperimentPendingRequests(@PathVariable("id") Long experimentId) throws Exception {
        return participationService.getExperimentPendingRequests(experimentId);
    }
    @PostMapping("myCreatedExperiments/pending-requests/accept-request/{id}")
    public Participation postAcceptJoinRequest(@PathVariable("id") Long participationId) throws Exception {
        return participationService.postAcceptJoinRequest(participationId);
    }

    //taking the experiment

    @PostMapping("/experiments-byEmail/participate/{id}")
    public Participation postParticipate(@RequestBody PostParticipateRequest postParticipateRequest, @PathVariable("id") Long experimentId) throws Exception {
        return participationService.postParticipate(postParticipateRequest, experimentId);
    }

    @GetMapping("myCreatedExperiments/joined/{id}")
    public List<Participation> getExperimentJoinedParticipations(@PathVariable("id") Long experimentId) throws Exception {
        return participationService.getExperimentJoinedParticipations(experimentId);
    }

    @GetMapping("myCreatedExperiments/taken/{id}")
    public List<Participation> getExperimentTakenParticipations(@PathVariable("id") Long experimentId) throws Exception {
        return participationService.getExperimentTakenParticipations(experimentId);
    }
}
