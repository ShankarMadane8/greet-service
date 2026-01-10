package com.example.greetservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

        @Value("${server.port:9091}")
        private String serverPort;

        @Bean
        public OpenAPI greetServiceOpenAPI() {
                Server localServer = new Server();
                localServer.setUrl("http://localhost:" + serverPort);
                localServer.setDescription("Local Development Server");

                Server gatewayServer = new Server();
                gatewayServer.setUrl("http://localhost:8082/greet-service");
                gatewayServer.setDescription("API Gateway Server");

                Contact contact = new Contact();
                contact.setEmail("support@microservices.com");
                contact.setName("Microservices Team");
                contact.setUrl("https://www.microservices.com");

                License mitLicense = new License()
                                .name("MIT License")
                                .url("https://choosealicense.com/licenses/mit/");

                Info info = new Info()
                                .title("Greet Service API")
                                .version("1.0.0")
                                .contact(contact)
                                .description("This API provides greeting functionality and student management operations. "
                                                +
                                                "It includes distributed scheduling with ShedLock, Kafka consumer integration, "
                                                +
                                                "and batch processing capabilities.")
                                .termsOfService("https://www.microservices.com/terms")
                                .license(mitLicense);

                return new OpenAPI()
                                .info(info)
                                .servers(List.of(localServer, gatewayServer));
        }
}
