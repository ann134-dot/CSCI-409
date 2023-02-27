package com.seniorproject.first.prototype.controller;

import com.seniorproject.first.prototype.entity.Experiment;
import com.seniorproject.first.prototype.entity.User;
import com.seniorproject.first.prototype.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

//    @PostMapping("/add-user")
//    public User addUser(@RequestBody User user){
//        return userService.addUser(user);
//    }
    @GetMapping("my-created-experiments")
    public List<Experiment> fetchMyCreatedExperiments(){
        return userService.fetchMyCreatedExperiments();
    }

}
