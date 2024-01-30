package com.ziggly.authservice.security;

import com.ziggly.authservice.repository.UserRepository;
import com.ziggly.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Optional<User> credential = userRepository.findByUserName(userName);
        return credential.map(UserDetailsImpl::new).orElseThrow(() -> new UsernameNotFoundException("user not found with name :" + userName));
    }
}
