package com.localpayment.lp_test_logs.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class HealthCheckController {
    @GetMapping("/ping")
    public ResponseEntity<JsonNode> ping() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode response = mapper.createObjectNode().put("message", "pong");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/exception")
    public ResponseEntity<JsonNode> exception() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode response = mapper.createObjectNode();
        if(true){
            throw new Exception();
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}