package com.demo.backend.controller;

import com.demo.backend.model.User;
import com.demo.backend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> body) {
        return authService.register(body);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        return authService.login(body);
    }

    @GetMapping("/getUserInfo")
    public ResponseEntity<?> getUserInfo(Authentication auth) {
        return authService.getUserInfo(auth);
    }

    @GetMapping("/getUserInfoOneChamps")
    public ResponseEntity<?> getUserEmail(Authentication auth) {
        return authService.getUserEmail(auth);
    }
    @GetMapping("/getAllUsers")
    public List<User> getAllUser() {
        return  authService.getAlluser();
    }
}
