package com.seniorproject.first.prototype.controller;

import com.seniorproject.first.prototype.entity.Experiment;
import com.seniorproject.first.prototype.entity.Participation;
import com.seniorproject.first.prototype.model.PostParticipateRequest;
import com.seniorproject.first.prototype.service.ParticipationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.parser.Entity;
import java.util.List;

@RestController
@RequestMapping("/api/v2/particpation")
public class ParticipationController {

    @Autowired
    private ParticipationService participationService;
//moved to ExperimentController
//    @GetMapping("/experiments-byEmail/{email}")
//    public List<Experiment> findExperimentsByEmail(@PathVariable("email") String creatorEmail){
//        return participationService.findExperimentsByEmail(creatorEmail);
//    }

    @GetMapping("/participate/{id}")
    public ResponseEntity<Object> getParticpate(@PathVariable("id") Long experimentId) throws Exception {
        return participationService.getParticipate(experimentId);
    }

    //joining the experiment
    @PostMapping("/join/{id}")
    public ResponseEntity<Object> postJoin(@PathVariable("id") Long experimentId) throws Exception {
        return participationService.postJoin(experimentId);
    }
    @GetMapping("/myCreatedExperiments/pending-requests/{id}")
    public ResponseEntity<Object> getExperimentPendingRequests(@PathVariable("id") Long experimentId) throws Exception {
        return participationService.getExperimentPendingRequests(experimentId);
    }
    @PostMapping("/myCreatedExperiments/pending-requests/accept-request/{id}")
    public ResponseEntity<Object> postAcceptJoinRequest(@PathVariable("id") Long participationId) throws Exception {
        return participationService.postAcceptJoinRequest(participationId);
    }
    @PostMapping("/myCreatedExperiments/pending-requests/reject-request/{id}")
    public ResponseEntity<Object> postrejectJoinRequest(@PathVariable("id") Long participationId) throws Exception {
        return participationService.postRejectJoinRequest(participationId);
    }

    //taking the experiment

    @PostMapping("/participate/{id}")
    public ResponseEntity<Object> postParticipate(@RequestBody PostParticipateRequest postParticipateRequest, @PathVariable("id") Long experimentId) throws Exception {
        return participationService.postParticipate(postParticipateRequest, experimentId);
    }

    @GetMapping("/myCreatedExperiments/joined/{id}")
    public List<Participation> getExperimentJoinedParticipations(@PathVariable("id") Long experimentId) throws Exception {
        return participationService.getExperimentJoinedParticipations(experimentId);
    }

    @GetMapping("/myCreatedExperiments/taken/{id}")
    public ResponseEntity<Object> getExperimentTakenParticipations(@PathVariable("id") Long experimentId) throws Exception {
        return participationService.getExperimentTakenParticipations(experimentId);
    }

    @GetMapping("/myRequests")
    public ResponseEntity<Object> getMyParticipationRequests(){
        return participationService.getMyParticipationRequests();
    }

    @GetMapping("/myTakenParticipation/{experimentId}")
    public ResponseEntity<Object> getMyTakenParticipation(@PathVariable("experimentId") Long experimentId){
        return participationService.getMyTakenParticipation(experimentId);
    }
}
