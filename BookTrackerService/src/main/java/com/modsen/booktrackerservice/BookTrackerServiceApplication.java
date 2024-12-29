package com.modsen.booktrackerservice;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@OpenAPIDefinition(
        info = @Info(
                title = "Book Tracker Service API",
                version = "1.0",
                description = "API for managing books tracker"
        ),
        servers = @Server(
                url = "http://localhost:8081",
                description = "Local server"
        )
)

@SpringBootApplication
public class BookTrackerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookTrackerServiceApplication.class, args);
    }

}
