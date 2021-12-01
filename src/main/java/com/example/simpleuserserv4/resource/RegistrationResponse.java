package com.example.simpleuserserv4.resource;

import com.example.simpleuserserv4.client.ActivationResponse;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RegistrationResponse {
    String token;
    ActivationResponse marketing;

    public RegistrationResponse(String token, ActivationResponse activationResponse) {
        this.token = token;
        this.marketing = activationResponse;
    }
}
