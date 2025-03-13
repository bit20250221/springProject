package org.spring.attraction.service;

import lombok.extern.slf4j.Slf4j;
import org.spring.attraction.dto.CustomUserDetails;
import org.spring.attraction.entity.User;
import org.spring.attraction.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userLoginId) throws UsernameNotFoundException {
        User user = userRepository.findByUserLoginId(userLoginId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with login ID: " + userLoginId));

        return new CustomUserDetails(user);
    }

}