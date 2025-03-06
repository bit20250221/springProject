package org.spring.attraction.service;

import org.spring.attraction.dto.user.CustomUserDetails;
import org.spring.attraction.entity.User;
import org.spring.attraction.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userLoginId) throws UsernameNotFoundException {

        User user = userRepository.findByUserLoginId(userLoginId);

        if (user == null) {
            throw new UsernameNotFoundException("User not found with login ID: " + userLoginId);
        }

        return new CustomUserDetails(user);
    }
}
