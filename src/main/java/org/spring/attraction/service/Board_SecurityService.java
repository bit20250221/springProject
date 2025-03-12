package org.spring.attraction.service;

import org.spring.attraction.dto.user.CustomUserDetails;
import org.spring.attraction.dto.user.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class Board_SecurityService {


    public UserDTO getUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.getPrincipal() instanceof CustomUserDetails){
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            return UserDTO.fromUser(userDetails.getUser());
        }
        return null;
    }
}
