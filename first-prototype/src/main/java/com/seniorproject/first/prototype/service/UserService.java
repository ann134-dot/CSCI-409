package com.seniorproject.first.prototype.service;

import com.seniorproject.first.prototype.entity.Experiment;
import com.seniorproject.first.prototype.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    public User addUser(User user);

    List<Experiment> fetchMyCreatedExperiments();

//    public User registerUser(User user);
}
