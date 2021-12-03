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
import com.example.simpleuserserv4.service.UserService;
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
import java.util.Objects;
import java.util.Optional;

@Service
@Scope("singleton")
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    private GroupRepository groupRepository;

    private AdServClient adServClient;

    private ModelMapper modelMapper;

    @Autowired
    protected PasswordEncoder passwordEncoder;

    @Autowired
    protected JWTProvider jwtProvider;

    @Autowired
    protected AuthenticationManager authenticationManager;

    @Autowired
    protected NotificationSender notificationSender;

    public UserServiceImpl(UserRepository userRepository, GroupRepository groupRepository, AdServClient adServClient, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.adServClient = adServClient;
        this.modelMapper = modelMapper;
    }

    @Override
    //TODO: Try functional style
    public User createUser(User user) {


        User savedUser = null;
        if (!userRepository.existsByUsername(user.getUsername())) {

            // Save in Repository
            UserEntity userEntity = modelMapper.map(user, UserEntity.class);

            if (!CollectionUtils.isEmpty(userEntity.getAddresses())) {
                for (AddressEntity addressEntity : userEntity.getAddresses()) {
                    addressEntity.setUser(userEntity);
                }
            }

            if (!CollectionUtils.isEmpty(user.getGroups())) {
                for (Group group : user.getGroups()) {
                    GroupEntity groupEntity = groupRepository.findByName(group.getName());
                    if (groupEntity == null) {
                        groupEntity = modelMapper.map(group, GroupEntity.class);
                    }
                    groupEntity.getUsers().add(userEntity);
                    userEntity.getGroups().add(groupEntity);
                }
            }

            UserEntity savedUserEntity = userRepository.save(userEntity);
            savedUser = modelMapper.map(savedUserEntity, User.class);
            System.out.println("Saved User: " + savedUserEntity);

            try {
                ActivationResponse activationResponse = adServClient.activate(new ActivationRequest(savedUser.getId()));
                System.out.println(activationResponse);
                savedUser.setMarketing(activationResponse);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            throw new ServiceException("Username already exists", HttpStatus.BAD_REQUEST);
        }
        return savedUser;
    }

    @Override
    // TODO: Implement Pagination
    public UserCollection findUsers(Optional<String> username, int pageSize, int page) {

        Pageable pageable = PageRequest.of((page - 1), pageSize, Sort.by("username").ascending());
        Page<UserEntity> pagedResult;

        if (username.isPresent()) {
            pagedResult = userRepository.findByFirstName(username.get(), pageable);
        } else {
            pagedResult = userRepository.findAll(pageable);
        }

        long totalItems = pagedResult.getTotalElements();
        long totalPages = pagedResult.getTotalPages();
        page = pagedResult.getNumber() + 1;
        pageSize = pagedResult.getNumberOfElements();
        List<UserEntity> userEntityList = pagedResult.getContent();
        List<User> userList = new ArrayList<>();

        if (!CollectionUtils.isEmpty(userEntityList)) {
            for (UserEntity userEntity : userEntityList) {
                User user = modelMapper.map(userEntity, User.class);
                userList.add(user);
            }
        }

        UserCollection userCollection = new UserCollection(totalItems, totalPages, page, pageSize, userList);

        return userCollection;
    }

    @Override
    public User getUser(Long id) {

        User user = null;

        Optional<UserEntity> optionalUserEntity = userRepository.findById(id);

        if (!optionalUserEntity.isPresent()) {
            throw new ServiceException("User Not Found", HttpStatus.NOT_FOUND);
        }
        user = modelMapper.map(optionalUserEntity.get(), User.class);

        return user;
    }


    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        try {
            String username = loginRequest.getUsername();
            String password = loginRequest.getPassword();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            String token = jwtProvider.createToken(username, userRepository.findByUsername(username).getRoles());
            return new LoginResponse(token);
        } catch (AuthenticationException e) {
            throw new ServiceException("Invalid username/password", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @Override
    public RegistrationResponse register(User user) {

        if (!userRepository.existsByUsername(user.getUsername())) {

            // Save in Repository
            UserEntity userEntity = modelMapper.map(user, UserEntity.class);
            userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
            UserEntity savedUserEntity = userRepository.save(userEntity);
            System.out.println("Saved User: " + savedUserEntity);

            // Call AdServ
            ActivationRequest activationRequest = new ActivationRequest(savedUserEntity.getId());
            System.out.println("Requested Activation: " + activationRequest);
            ActivationResponse activationResponse = null;
            try {
                activationResponse = adServClient.activate(new ActivationRequest(savedUserEntity.getId()));
                System.out.println("ActivationResponse: " + activationResponse);
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Create Token
            String token = jwtProvider.createToken(userEntity.getUsername(), userEntity.getRoles());




            // Send Notification
            if (userEntity.getEmail() != null) {
                notificationSender.send(new Notification(NotificationType.EMAIL, userEntity.getEmail(), "Account Created!"));
            }
            if (userEntity.getMobile() != null) {
                notificationSender.send(new Notification(NotificationType.SMS, userEntity.getMobile(), "Account Created!"));
            }

            System.out.println("-------------------------------------------------");
            System.out.println("User Registration completed and response sent!");
            System.out.println("Notification will be sent asynchronously");
            System.out.println("-------------------------------------------------");



            return new RegistrationResponse(token, activationResponse);

        } else {
            throw new ServiceException("Username already exists", HttpStatus.BAD_REQUEST);
        }
    }


    @Override
    public String refresh(String username) {
        return jwtProvider.createToken(username, userRepository.findByUsername(username).getRoles());
    }


}
