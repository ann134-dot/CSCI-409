package org.experimentV1.controllers;

import org.experimentV1.entity.Experiment;
import org.experimentV1.entity.ObjectHolder;
import org.experimentV1.services.ExperimentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/experiment")
public class ExperimentController {

    private final ExperimentServiceImpl experimentService;

    @Autowired
    public ExperimentController(ExperimentServiceImpl experimentService) {
        this.experimentService = experimentService;
    }


    @PostMapping("/postRandomV2")
    public ResponseEntity<Object> randomWords(@RequestBody Experiment experiment){
        return experimentService.saveRandomizedExperimentV2(experiment);
    }

    @PostMapping("/newExperiment")
    public ResponseEntity<Object> createExperiment(@RequestBody Experiment experiment){
        return experimentService.createExperiment(experiment);
    }

    @GetMapping("/getExperiment/{id}")
    public ResponseEntity<Object> getExperimentById(@PathVariable Long id){
        return experimentService.getExperiment(id);
    }

    @GetMapping("/getAllExperiment")
    public ResponseEntity<Object> getAllExperiments(){
        return experimentService.getAllExperiments();
    }

    @DeleteMapping("/deleteById")
    public ResponseEntity<Object> deleteExperimentById(@RequestParam Long id){
        return experimentService.deleteExperimentById(id);
    }

    // Does not work
    @DeleteMapping("/deleteByUser")
    public ResponseEntity<Object> deleteExperimentByUser(@RequestParam String user){
        return experimentService.deleteExperimentByUser(user);
    }


    // Ex: get 3 words which frequencies are between 3000 and 5000. List: {fatal, frustrate, overwhelm}
    @PostMapping("/postExperimentFrequencyRange")
    public ResponseEntity<Object> saveExperimentWithFrequencyRange(@RequestBody Experiment experiment){
        return experimentService.createExperimentWithFrequencyRange(experiment);
    }

    // Change RequestParam to requestBody
    // Ex: get 1 word with length=3 and 2 words with length=5. List will consist of 3 words:{bag, phase, drink}
    @PostMapping("/postExperimentLength")
    public ResponseEntity<Object> saveExperimentWithLength(@RequestBody ObjectHolder obj){
        Experiment experiment = obj.getExperiment();
        List<Integer> numberOfWordsPerLength = obj.getNumberOfWordsPerLength();
        return experimentService.createExperimentWithLength(experiment, numberOfWordsPerLength);
    }

    // Ex:
    @PostMapping("/postExperimentFrequencyAndLength")
    public ResponseEntity<Object> saveExperimentWithLengthAndFrequency(@RequestBody Experiment experiment, @RequestParam List<Integer> numberOfWordsPerLength){
        return experimentService.createExperimentWithLengthAndFrequency(experiment, numberOfWordsPerLength);
    }

}
