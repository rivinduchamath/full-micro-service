package com.cog.gateway.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallbackController {

    @GetMapping(value = "/fallback")
    public ResponseEntity<?> fallback() {
        return ResponseHandler.responseBuilder("Server not available", "5000", HttpStatus.FAILED_DEPENDENCY, null);
    }

}
