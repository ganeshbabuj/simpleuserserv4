package com.example.simpleuserserv4.service.impl;

import com.example.simpleuserserv4.client.ActivationRequest;
import com.example.simpleuserserv4.client.ActivationResponse;
import com.example.simpleuserserv4.client.AdServClient;
import com.example.simpleuserserv4.entity.AddressEntity;
import com.example.simpleuserserv4.entity.GroupEntity;
import com.example.simpleuserserv4.entity.UserEntity;
import com.example.simpleuserserv4.exception.ServiceException;
import com.example.simpleuserserv4.messaging.NotificationSender;
import com.example.simpleuserserv4.repository.GroupRepository;
import com.example.simpleuserserv4.repository.UserRepository;
import com.example.simpleuserserv4.resource.*;
import com.example.simpleuserserv4.security.JWTProvider;
import com.example.simpleuserserv4.service.HelperService;
import com.example.simpleuserserv4.service.UserService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class HelperServiceImpl implements HelperService {

    private int accessCount = 1;

    private AdServClient adServClient;
    private ModelMapper modelMapper;

    public HelperServiceImpl(AdServClient adServClient, ModelMapper modelMapper) {
        this.adServClient = adServClient;
        this.modelMapper = modelMapper;
    }

    @CircuitBreaker(name = "adserv", fallbackMethod = "fallbackActivate")
    public ActivationResponse activateAdPlan(ActivationRequest activationRequest) {

        ActivationResponse activationResponse = null;
        try {
            System.out.println("accessCount: " + accessCount);
            accessCount++;
            activationResponse = adServClient.activate(new ActivationRequest(activationRequest.getUserId()));
            System.out.println(activationResponse);

        } catch (RuntimeException e) {
            e.printStackTrace();
            throw e;
        }
        return activationResponse;
    }

    public ActivationResponse fallbackActivate (ActivationRequest activationRequest, Throwable t) {
        System.out.println("FallbackActivate");
        // put this activation req in a queue and send fallback response
        return new ActivationResponse(0L, "PENDING");
    }


}
