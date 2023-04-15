package com.seniorproject.first.prototype.service;

import com.seniorproject.first.prototype.entity.Experiment;
import com.seniorproject.first.prototype.entity.User;
//import com.seniorproject.first.prototype.model.UserModel;
import com.seniorproject.first.prototype.model.UserInfo;
import com.seniorproject.first.prototype.repository.UserRepository;
import com.seniorproject.first.prototype.util.ResponseHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Service @RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        User user = userRepository.findUserByUserEmail(userEmail).get();
        if(user == null){
            throw new UsernameNotFoundException("User not found in the DB");
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("SIMPLE_USER"));
        return new org.springframework.security.core.userdetails.User(user.getUserEmail(), user.getPassword(), authorities);
    }

    @Override
    public User addUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public List<Experiment> fetchMyCreatedExperiments() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        User user = userRepository.findUserByUserEmail(currentUserName).get();
        return user.getCreatedExperiments();
    }

    @Override
    public ResponseEntity<Object> getMyUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findUserByUserEmail(authentication.getName()).get();
        UserInfo userInfo = new UserInfo(user.getUserEmail(), user.getFirstName(), user.getLastName(), user.getAge(), user.getGender(), user.getDegree());
        return ResponseHandler.generateResponse("Returning userInfo for the currently logged in user", HttpStatus.OK, userInfo);
    }

    @Override
    public ResponseEntity<Object> updateMyUserInfo(UserInfo userInfo) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User dbUser = userRepository.findUserByUserEmail(authentication.getName()).get();

        //Update FirstName
        if(Objects.nonNull(userInfo.getFirstName()) && !"".equalsIgnoreCase(userInfo.getFirstName())){
            dbUser.setFirstName(userInfo.getFirstName());
        }
        //Update LastName
        if(Objects.nonNull(userInfo.getLastName()) && !"".equalsIgnoreCase(userInfo.getLastName())){
            dbUser.setLastName(userInfo.getLastName());
        }
        //Update Age
        if(Objects.nonNull(userInfo.getAge()) && userInfo.getAge() != dbUser.getAge()){
            dbUser.setAge(userInfo.getAge());
        }
        //Update gender
        if(Objects.nonNull(userInfo.getGender()) && !"".equalsIgnoreCase(userInfo.getGender())){
            dbUser.setGender(userInfo.getGender());
        }
        //Update Degree
        if(Objects.nonNull(userInfo.getDegree()) && !"".equalsIgnoreCase(userInfo.getDegree())){
            dbUser.setDegree(userInfo.getDegree());
        }
        userRepository.save(dbUser);
        return ResponseHandler.generateResponse("Updated user's profile info", HttpStatus.OK, dbUser);
    }

//    @Override
//    public User registerUser(User user) {
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        return userRepository.save(user);
//    }
}
