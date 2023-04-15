package com.seniorproject.first.prototype.service;

import com.seniorproject.first.prototype.entity.Experiment;
import com.seniorproject.first.prototype.entity.User;
import com.seniorproject.first.prototype.model.UserInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    public User addUser(User user);

    List<Experiment> fetchMyCreatedExperiments();

    ResponseEntity<Object> getMyUserInfo();

    ResponseEntity<Object> updateMyUserInfo(UserInfo userInfo);

//    public User registerUser(User user);
}
