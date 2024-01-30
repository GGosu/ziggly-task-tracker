package com.ziggly.authservice.repository;

import com.ziggly.model.user.User;
import com.ziggly.model.user.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile,Integer> {
    Optional<UserProfile> findByUser(User user);
}
