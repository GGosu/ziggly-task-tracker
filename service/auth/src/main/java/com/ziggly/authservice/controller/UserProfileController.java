package com.ziggly.authservice.controller;

import com.ziggly.authservice.api.UserProfileService;
import com.ziggly.authservice.service.UserProfileServiceImpl;
import com.ziggly.model.dto.UserProfileDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserProfileController implements UserProfileService {
    @Autowired
    private UserProfileServiceImpl userProfileService;
    @Override
    public ResponseEntity<UserProfileDTO> getUserProfile(Integer userID, String token, HttpServletRequest request) {
        return userProfileService.getUserProfile(userID, token, request);
    }

    @Override
    public ResponseEntity<String> updateUserProfile(UserProfileDTO userProfileDTO, String token, HttpServletRequest request) {
        return userProfileService.updateUserProfile(userProfileDTO, token, request);
    }

    @Override
    public ResponseEntity<String> changePassword(String oldPassword, String newPassword, String token, HttpServletRequest request) {
        return userProfileService.changePassword(oldPassword, newPassword, token, request);
    }
}
