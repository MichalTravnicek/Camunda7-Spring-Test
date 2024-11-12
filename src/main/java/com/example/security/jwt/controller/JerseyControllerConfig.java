package com.example.security.jwt.controller;

import org.camunda.bpm.spring.boot.starter.rest.CamundaJerseyResourceConfig;
import org.springframework.stereotype.Component;

@Component
public class JerseyControllerConfig extends CamundaJerseyResourceConfig {

    @Override
    protected void registerAdditionalResources() {
        registerClasses(JwtAuthenticationController.class);
    }
}
