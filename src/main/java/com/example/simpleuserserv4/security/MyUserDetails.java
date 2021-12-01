package com.example.simpleuserserv4.security;

import com.example.simpleuserserv4.entity.UserEntity;
import com.example.simpleuserserv4.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetails implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final UserEntity userEntity = userRepository.findByUsername(username);

        if (userEntity == null) {
            throw new UsernameNotFoundException(username + " NOT FOUND");
        }
        System.out.println("loadUserByUsername: " + userEntity);

        return org.springframework.security.core.userdetails.User
                .withUsername(username)
                .password(userEntity.getPassword())
                .authorities(userEntity.getRoles())
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }

}
