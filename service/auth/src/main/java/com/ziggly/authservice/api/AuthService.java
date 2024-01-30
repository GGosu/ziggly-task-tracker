package com.ziggly.authservice.api;

import com.ziggly.model.dto.AuthRequest;
import com.ziggly.model.dto.SignUpRequest;
import com.ziggly.model.dto.UserSessionDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface AuthService {


    @PostMapping("api/auth/sign-up")
    public ResponseEntity<String> signUp(@RequestBody SignUpRequest signUpRequest);

    @PostMapping("api/auth/login")
    public ResponseEntity<String> login(@RequestBody AuthRequest authRequest, HttpServletRequest request);
    @PostMapping("api/auth/logout")
    public ResponseEntity<String> logout(@RequestParam String token, HttpServletRequest request);

    @GetMapping("/api/auth/validate")
    public ResponseEntity<String> tokenValidate(@RequestParam String token, HttpServletRequest request);


    @GetMapping("/api/auth/sessions")
    public ResponseEntity<List<UserSessionDTO>> getSessions(@RequestParam String token, HttpServletRequest request);

    @DeleteMapping("/api/auth/sessions/{sessionId}")
    public ResponseEntity<String> deleteSession(@PathVariable Integer sessionId, @RequestParam String token, HttpServletRequest request);


}
