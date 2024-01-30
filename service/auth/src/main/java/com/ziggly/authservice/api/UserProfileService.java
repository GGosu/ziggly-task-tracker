package com.ziggly.authservice.api;

import com.ziggly.model.dto.UserProfileDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

public interface UserProfileService {
    @GetMapping("/api/profile/{userID}")
    public ResponseEntity<UserProfileDTO> getUserProfile(@PathVariable Integer userID, String token, HttpServletRequest request);

    @PutMapping("/api/profile")
    public ResponseEntity<String> updateUserProfile(@RequestBody UserProfileDTO userProfileDTO, String token, HttpServletRequest request);

    @PutMapping("/api/profile/change-password")
    public ResponseEntity<String> changePassword(String oldPassword, String newPassword, String token, HttpServletRequest request);




}
