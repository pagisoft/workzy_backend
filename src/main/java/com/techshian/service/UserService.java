package com.techshian.service;

import com.techshian.model.User;
import com.techshian.model.UserDto;

import java.util.List;

import org.springframework.http.ResponseEntity;

public interface UserService {

    ResponseEntity<?> save(UserDto user);
    List<User> findAll();
    void delete(long id);
    User findOne(String username);

    User findById(Long id);
	ResponseEntity<?> saveTalent(UserDto user);
	ResponseEntity<?> update(User user);
	User loadUserByEmail(String id);
}
