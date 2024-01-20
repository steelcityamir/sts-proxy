package com.stsproxy.api.controller;

import com.stsproxy.api.data.UserCredentials;
import com.stsproxy.api.service.AwsStsService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.services.sts.model.Credentials;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AwsStsService awsStsService;

    @PostMapping
    public ResponseEntity<?> authenticateVendor(@RequestBody @Valid UserCredentials userCredentials) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userCredentials.getUsername(),
                        userCredentials.getPassword()
                )
        );

        Credentials stsCredentials = awsStsService.getTemporaryCredentials();
        return ResponseEntity.ok(stsCredentials);
    }
}

