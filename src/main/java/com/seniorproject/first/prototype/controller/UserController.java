package com.seniorproject.first.prototype.controller;

import com.seniorproject.first.prototype.entity.Experiment;
import com.seniorproject.first.prototype.entity.User;
import com.seniorproject.first.prototype.model.UserInfo;
import com.seniorproject.first.prototype.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v2/user")
public class UserController {

    @Autowired
    private UserService userService;

//    @PostMapping("/add-user")
//    public User addUser(@RequestBody User user){
//        return userService.addUser(user);
//    }
    @GetMapping("/my-created-experiments")
    public List<Experiment> fetchMyCreatedExperiments(){ // move this to the experiment controller
        return userService.fetchMyCreatedExperiments();
    }

    @GetMapping("/myUserInfo")
    public ResponseEntity<Object> getMyUserInfo(){
        return userService.getMyUserInfo();
    }

    @PutMapping("/myUserInfo")
    public ResponseEntity<Object> updateMyUserInfo(@RequestBody UserInfo userInfo){
        return userService.updateMyUserInfo(userInfo);
    }

}
