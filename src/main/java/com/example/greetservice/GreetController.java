package com.example.greetservice;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Value;

@RestController
@Tag(name = "Greet", description = "Greeting operations API")
public class GreetController {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(GreetController.class);

    @Value("${msg:Default Greet}")
    private String message;

    @Value("${server.port}")
    private String port;

    @Operation(summary = "Get greeting message", description = "Returns a greeting message from the Greet Service with port information. "
            +
            "This endpoint demonstrates service-to-service communication and configuration management.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved greeting message")
    })
    @GetMapping("/greet")
    // @org.springframework.cache.annotation.Cacheable(value = "greet-cache")
    public String greet() {
        log.info("Request received at Greet Service");
        log.info("Processing request on port: {}", port);
        return message + " from Greet Service (Port: " + port + ")";
    }
}
