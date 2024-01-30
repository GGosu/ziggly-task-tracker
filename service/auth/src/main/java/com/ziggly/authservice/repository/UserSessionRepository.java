package com.ziggly.authservice.repository;

import com.ziggly.model.user.User;
import com.ziggly.model.user.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserSessionRepository extends JpaRepository<UserSession,Integer> {

    List<UserSession> findByUser(User user);

}
