package com.example.simpleuserserv4.resource;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
public class LoginRequest {

    private String username;
    private String password;
}
