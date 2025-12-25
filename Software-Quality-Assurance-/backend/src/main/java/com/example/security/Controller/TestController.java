package com.example.security.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/debug")
@RequiredArgsConstructor
@Slf4j
public class TestController {

    @GetMapping("/security")
    public ResponseEntity<Map<String, Object>> checkSecurity(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();

        response.put("path", request.getServletPath());
        response.put("method", request.getMethod());
        response.put("origin", request.getHeader("Origin"));
        response.put("contentType", request.getHeader("Content-Type"));
        response.put("status", "ACCESS_GRANTED");
        response.put("timestamp", System.currentTimeMillis());

        log.info("✅ Debug endpoint accessed: {}", request.getServletPath());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/echo")
    public ResponseEntity<Map<String, Object>> echo(@RequestBody Map<String, Object> body, HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>(body);
        response.put("received", true);
        response.put("path", request.getServletPath());
        response.put("server", "Spring Boot");

        log.info("✅ Echo endpoint: {}", body);

        return ResponseEntity.ok(response);
    }
}