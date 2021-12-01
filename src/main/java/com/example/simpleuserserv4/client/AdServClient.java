package com.example.simpleuserserv4.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

//@FeignClient(url = "${ad.service.url}", name = "adserv", path = "/v1/marketing")
@FeignClient(name = "adserv", path = "/v1/marketing")
public interface AdServClient {

    @PostMapping("/activate")
    public ActivationResponse activate(@RequestBody ActivationRequest activationRequest);

}
