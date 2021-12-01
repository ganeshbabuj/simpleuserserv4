package com.example.simpleuserserv4.resource;

import com.example.simpleuserserv4.client.ActivationResponse;
import com.example.simpleuserserv4.security.Role;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private long id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String mobile;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    List<Role> roles;

    private Set<Address> addresses;
    private Set<Group> groups;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ActivationResponse marketing;

}
