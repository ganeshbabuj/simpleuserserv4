package com.example.simpleuserserv4.service;

import com.example.simpleuserserv4.resource.*;

import java.util.Optional;

public interface UserService {

    User createUser(User user);
    User getUser(Long id);
    UserCollection findUsers(Optional<String> firstName, int pageSize, int page);

    RegistrationResponse register(User user);
    LoginResponse login(LoginRequest loginRequest);
    String refresh(String username);
}
