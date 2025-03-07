package org.spring.attraction.service;

import lombok.extern.slf4j.Slf4j;
import org.spring.attraction.dto.user.UserDTO;
import org.spring.attraction.entity.User;
import org.spring.attraction.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import static org.spring.attraction.ENUM.Grade.bronze;
import static org.spring.attraction.ENUM.UserType.manager;
import static org.spring.attraction.ENUM.UserType.nomal;

@Slf4j
@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public void registerProcess(UserDTO userDTO) {

        boolean isUser = userRepository.existsByUserLoginId(userDTO.getUserLoginId());
        if(isUser) {
            return;
        }

        User user = new User();
        log.info(userDTO.getId().toString());
        log.info(userDTO.getUserLoginId());
        log.info(userDTO.getPass());
        log.info(userDTO.getBirthDate().toString());

        user.setId(userDTO.getId());
        user.setUserLoginId(userDTO.getUserLoginId());
        user.setUserType(nomal);
        user.setPass(bCryptPasswordEncoder.encode(userDTO.getPass()));
        user.setBirthDate(userDTO.getBirthDate());
        user.setGrade(bronze);

        userRepository.save(user);
    }
}