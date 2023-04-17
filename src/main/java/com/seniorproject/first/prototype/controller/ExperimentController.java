package com.seniorproject.first.prototype.controller;

import com.seniorproject.first.prototype.entity.Experiment;
import com.seniorproject.first.prototype.service.ExperimentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v2/experiment")
public class ExperimentController {

    private final ExperimentService experimentService;

    @Autowired
    public ExperimentController(ExperimentService experimentService) {
        this.experimentService = experimentService;
    }

    @PostMapping("/postExperiment")
    public ResponseEntity<Object> createExperiment(@RequestBody Experiment experiment){
        return experimentService.createExperiment(experiment);
    }

    @PostMapping("/postRandomV2")
    public ResponseEntity<Object> createExperimentWithRandomWords(@RequestBody Experiment experiment){
        return experimentService.createRandomizedExperiment(experiment);
    }

    @PostMapping("/postExperimentWithParam")
    public ResponseEntity<Object> createExperimentWithLengthAndFrequency(@RequestBody Experiment experiment){
        return experimentService.createExperimentLengthAndFrequency(experiment);
    }

    @GetMapping("/getAllExperiments")
    public ResponseEntity<Object> getAllExperiments(){
        return experimentService.getAllExperiments();
    }

    @GetMapping("/myCreatedExperiments/id/{id}")
    public ResponseEntity<Object> getMyCreatedExperimentById(@PathVariable("id") Long experimentId){
        return experimentService.getMyExperimentById(experimentId);
    }
    @GetMapping("/myCreatedExperiments")
    public ResponseEntity<Object> getMyCreatedExperiments(){
        return experimentService.getAllMyExperiments();
    }

    @GetMapping("/UserPendingJoinExperiments")
    public List<Experiment> getMyPendingJoinExperiments(){
        return experimentService.getMyPendingJoinExperiments();
    }

    @GetMapping("/UserJoinedExperiments")
    public List<Experiment> getMyJoinedExperiments(){
        return experimentService.getMyJoinedExperiments();
    }

    @GetMapping("/UserTakenExperiments")
    public ResponseEntity<Object> getMyTakenExperiments(){
        return experimentService.getMyTakenExperiments();
    }

    @DeleteMapping("/myCreatedExperiments/{id}")
    public ResponseEntity<Object>  deleteMyCreatedExperimentById(@PathVariable("id") Long experimentId) throws Exception {
        return experimentService.deleteMyCreatedExperimentById(experimentId);
    }

    @PutMapping("/myCreatedExperiments/{id}")
    public ResponseEntity<Object> updateMyCreatedExperimentById(@PathVariable("id") Long experimentId, @RequestBody Experiment experiment) throws Exception {
        return experimentService.updateMyCreatedExperimentById(experimentId, experiment);
    }

    @GetMapping("/myCreatedExperiments/{name}")
    public ResponseEntity<Object> getMyCreatedExperimentByExperimentName(@PathVariable("name") String experimentName){
        return experimentService.getMyCreatedExperimentByExperimentName(experimentName);
    }

    @GetMapping("/experiments/{email}")
    public ResponseEntity<Object> findExperimentsByEmail(@PathVariable("email") String creatorEmail){
        return experimentService.getExperimentsByEmail(creatorEmail);
    }

    @GetMapping("/experimentStatistics/{id}")
    public ResponseEntity<Object> getExperimentStatistics(@PathVariable("id") Long id){
        return experimentService.getExperimentStatistics(id);
    }
}
