package com.ziggly.authservice.repository;

import com.ziggly.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {
    Optional<User> findByUserName(String userName);

    @NonNull
    Optional<User> findById(Integer id);
    Optional<User> findByEmail(String email);
}
