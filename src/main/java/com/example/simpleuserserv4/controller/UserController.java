package com.example.simpleuserserv4.controller;

import com.example.simpleuserserv4.client.ActivationRequest;
import com.example.simpleuserserv4.client.ActivationResponse;
import com.example.simpleuserserv4.resource.*;
import com.example.simpleuserserv4.service.HelperService;
import com.example.simpleuserserv4.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RestController
@RequestMapping("/v1/account")
public class UserController {

    private UserService userService;
    private HelperService helperService;

    public UserController(UserService userService, HelperService helperService) {
        this.userService = userService;
        this.helperService = helperService;
    }

    @PostMapping("/users")
    @ResponseStatus(code = HttpStatus.CREATED)
    public User create(@RequestBody User user) {
        return userService.createUser(user);
    }

    @GetMapping("/users/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public User read(@PathVariable("id") Long id) {
        return userService.getUser(id);
    }

    @GetMapping(value = "/users")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(code = HttpStatus.OK)
    public UserCollection search(@RequestParam(name = "username", required = false) Optional<String> username,
                                 @RequestParam(name = "page_size", required = false, defaultValue = "10") int pageSize,
                                 @RequestParam(name = "page", required = false, defaultValue = "1") int page) {

        System.out.println("Looking for: " + username);
        UserCollection userCollection = userService.findUsers(username, pageSize, page);
        return userCollection;

    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {
        return userService.login(loginRequest);
    }

    @PostMapping("/register")
    public RegistrationResponse register(@RequestBody User user) {
        return userService.register(user);
    }

    @GetMapping("/refresh")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public String refresh(HttpServletRequest req) {
        return userService.refresh(req.getRemoteUser());
    }


    @PostMapping("/test-activate")
    // Just for testing circuitbreaker.. this is not correct method to be in userserv as Single responsibility principle
    public ActivationResponse testAdPlanActivation(@RequestBody ActivationRequest activationRequest) {
        return helperService.activateAdPlan(activationRequest);
    }



}


