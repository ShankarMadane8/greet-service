package com.example.greetservice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Value;

@RestController
public class GreetController {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(GreetController.class);

    @Value("${msg:Default Greet}")
    private String message;

    @Value("${server.port}")
    private String port;

    @GetMapping("/greet")
    // @org.springframework.cache.annotation.Cacheable(value = "greet-cache")
    public String greet() {
        log.info("Request received at Greet Service");
        log.info("Processing request on port: {}", port);
        return message + " from Greet Service (Port: " + port + ")";
    }
}
