package com.sumittuladhar.wf.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Hello Controller", description = "Simple test controller to verify API setup")
public class HelloController {

    @GetMapping("/api/hello")
    @Operation(summary = "Get hello message", description = "Returns a friendly greeting message")
    public HelloResponse sayHello() {
        return new HelloResponse("Welcome to your Spring Boot 4 API project!");
    }

    public static class HelloResponse {
        private String message;

        public HelloResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
