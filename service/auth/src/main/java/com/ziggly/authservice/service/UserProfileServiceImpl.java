package com.ziggly.authservice.service;

import com.ziggly.authservice.api.UserProfileService;
import com.ziggly.authservice.repository.UserProfileRepository;
import com.ziggly.authservice.repository.UserRepository;
import com.ziggly.model.dto.UserProfileDTO;
import com.ziggly.model.user.User;
import com.ziggly.model.user.UserProfile;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private AuthServiceImpl authService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public ResponseEntity<UserProfileDTO> getUserProfile(Integer userID, String token, HttpServletRequest request) {

        ResponseEntity <String> validationResponse = authService.tokenValidate(token, request);

        if(validationResponse.getStatusCode() == HttpStatus.ACCEPTED){
            User user = userRepository.findById(userID).orElse(null);
            if(user != null){

                UserProfile userProfile = userProfileRepository.findByUser(user).orElse(null);

                if (userProfile != null){
                    UserProfileDTO userProfileDTO = new UserProfileDTO(
                            user.getUserName(), userProfile.getFirstName(),
                            userProfile.getLastName(), userProfile.getAvatarURL(),
                            userProfile.getTitle(), userProfile.getBio(),
                            userProfile.getCreatedAt()
                    );

                    return ResponseEntity
                            .status(HttpStatus.OK)
                            .body(userProfileDTO);
                }
            }
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(null);
        }else{
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(null);
        }

    }


    @Override
    public ResponseEntity<String> updateUserProfile(UserProfileDTO userProfileDTO, String token, HttpServletRequest request) {
        Integer userID = authService.getUserIdFromToken(token);

        ResponseEntity <String> validationResponse = authService.tokenValidate(token, request);

        if(validationResponse.getStatusCode() == HttpStatus.ACCEPTED){
            User user = userRepository.findById(userID).orElse(null);
            if(user != null){

                UserProfile userProfile = userProfileRepository.findByUser(user).orElse(null);

                if (userProfile != null){
                    if (userProfileDTO.getFirstName() != null) {
                        userProfile.setFirstName(userProfileDTO.getFirstName());
                    }
                    if (userProfileDTO.getLastName() != null) {
                        userProfile.setLastName(userProfileDTO.getLastName());
                    }
                    if (userProfileDTO.getAvatarURL() != null) {
                        userProfile.setAvatarURL(userProfileDTO.getAvatarURL());
                    }
                    if (userProfileDTO.getTitle() != null) {
                        userProfile.setTitle(userProfileDTO.getTitle());
                    }
                    if (userProfileDTO.getBio() != null) {
                        userProfile.setBio(userProfileDTO.getBio());
                    }

                    userProfileRepository.save(userProfile);

                    return ResponseEntity
                            .status(HttpStatus.ACCEPTED)
                            .body("User Profile updated!");
                }
            }
        }

        return ResponseEntity
                .status(HttpStatus.NOT_ACCEPTABLE)
                .body("User Profile update failed!");
    }

    @Override
    public ResponseEntity<String> changePassword(String oldPassword, String newPassword, String token, HttpServletRequest request) {

        if(authService.isValidPassword(newPassword)){

            ResponseEntity <String> validationResponse = authService.tokenValidate(token, request);

            if(validationResponse.getStatusCode() == HttpStatus.ACCEPTED) {
                Integer userID = authService.getUserIdFromToken(token);

                User user = userRepository.findById(userID).orElse(null);
                if(user != null){
                    if (passwordEncoder.matches(oldPassword, user.getPassword())) {
                        user.setPassword(passwordEncoder.encode(newPassword));
                        userRepository.save(user);

                        return ResponseEntity
                                .status(HttpStatus.ACCEPTED)
                                .body("Password updated!");
                    }
                }
            }
        }

        return ResponseEntity
                .status(HttpStatus.NOT_ACCEPTABLE)
                .body("Password update failed!");
    }
}
