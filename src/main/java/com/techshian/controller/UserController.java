package com.techshian.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.techshian.controller.BaseController.BaseController;
import com.techshian.model.User;
import com.techshian.model.UserDto;
import com.techshian.service.UserService;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/secure")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    //@Secured({"ROLE_ADMIN", "ROLE_USER"})
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value="/users", method = RequestMethod.GET)
    public List<User> listUser(){
        return userService.findAll();
    }

    //@Secured("ROLE_USER")
    //@PreAuthorize("hasRole('USER')")
    ////@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @RequestMapping(value = "/userProfile", method = RequestMethod.GET)
    public User getOne() throws JsonProcessingException, JSONException{
        return userService.loadUserByEmail( getLoggedInUser());
    }




}
