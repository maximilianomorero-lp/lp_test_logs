package com.localpayment.lp_test_logs.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class HealthCheckController {

    @Autowired
    private ObjectMapper mapper;

    @GetMapping("/ping")
    public ResponseEntity<JsonNode> ping() {
        JsonNode response = mapper.createObjectNode().put("message", "pong");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/exception")
    public ResponseEntity<JsonNode> exception() throws Exception {
        throw new Exception("Test exception endpoint");
    }
}