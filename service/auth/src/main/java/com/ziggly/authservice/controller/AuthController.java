package com.ziggly.authservice.controller;

import com.ziggly.authservice.api.AuthService;
import com.ziggly.authservice.service.AuthServiceImpl;
import com.ziggly.model.dto.AuthRequest;
import com.ziggly.model.dto.SignUpRequest;
import com.ziggly.model.dto.UserSessionDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
//@RequestMapping("/auth")
public class AuthController implements AuthService {
    @Autowired
    private AuthServiceImpl authService;

    @Override
    public ResponseEntity<String> signUp(SignUpRequest signUpRequest) {
        return authService.signUp(signUpRequest);
    }

    @Override
    public ResponseEntity<String> login(AuthRequest authRequest, HttpServletRequest request) {
        return authService.login(authRequest, request);
    }

    @Override
    public ResponseEntity<String> logout(String token, HttpServletRequest request) {
        return authService.logout(token, request);
    }

    @Override
    public ResponseEntity<String> tokenValidate(String token, HttpServletRequest request) {
        return authService.tokenValidate(token, request);
    }

    @Override
    public ResponseEntity<List<UserSessionDTO>> getSessions(String token, HttpServletRequest request) {
        return authService.getSessions(token, request);
    }

    @Override
    public ResponseEntity<String> deleteSession(Integer sessionId, String token, HttpServletRequest request) {
        return authService.deleteSession(sessionId, token, request);
    }
}
