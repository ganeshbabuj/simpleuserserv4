package com.example.simpleuserserv4.resource;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
public class LoginResponse {

    String token;

    public LoginResponse(String token) {
        this.token = token;
    }
}
