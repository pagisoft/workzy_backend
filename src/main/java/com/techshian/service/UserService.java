package com.techshian.service;

import com.techshian.model.User;
import com.techshian.model.UserDto;

import java.util.List;

public interface UserService {

    User save(UserDto user);
    List<User> findAll();
    void delete(long id);
    User findOne(String username);

    User findById(Long id);
}
