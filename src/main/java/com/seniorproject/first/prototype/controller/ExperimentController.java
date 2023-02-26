package com.seniorproject.first.prototype.controller;

import com.seniorproject.first.prototype.entity.Experiment;
import com.seniorproject.first.prototype.service.ExperimentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ExperimentController {

    @Autowired
    private ExperimentService experimentService;

    @PostMapping("/create-experiment")
    public Experiment createExperiment(@RequestBody Experiment experiment){
        return experimentService.createExperiment(experiment);
    }

}
