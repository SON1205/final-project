package com.ssafy.urs.controller;

import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/secure")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class SecureController {

    @GetMapping("/check")
    public ResponseEntity<Map<String, Object>> checkSession(HttpSession session) {
        if (session != null && session.getAttribute("userId") != null) {
            Map<String, Object> response = new HashMap<>();
            response.put("userId", session.getAttribute("userId"));
            response.put("userName", session.getAttribute("userName"));
            response.put("status", "Authenticated");
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("status", "Unauthorized"));
    }

}

