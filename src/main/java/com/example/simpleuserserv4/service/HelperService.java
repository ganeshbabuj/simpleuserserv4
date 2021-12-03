package com.example.simpleuserserv4.service;

import com.example.simpleuserserv4.client.ActivationRequest;
import com.example.simpleuserserv4.client.ActivationResponse;

public interface HelperService {

    ActivationResponse activateAdPlan(ActivationRequest activationRequest);
}
