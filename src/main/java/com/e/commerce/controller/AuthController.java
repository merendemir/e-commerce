package com.e.commerce.controller;

import com.e.commerce.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<Object> loginForUser(@RequestParam String email,
                                               @RequestParam String password) {
        return ResponseEntity.ok(authService.login(email, password));
    }

    @PostMapping("/seller/login")
    public ResponseEntity<Object> loginForSeller(@RequestParam String email,
                                                 @RequestParam String password) {
        return ResponseEntity.ok(authService.loginForSeller(email, password));
    }
}
