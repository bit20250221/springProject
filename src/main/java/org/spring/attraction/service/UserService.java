package org.spring.attraction.service;

import lombok.extern.slf4j.Slf4j;
import org.spring.attraction.dto.user.UserDTO;
import org.spring.attraction.entity.User;
import org.spring.attraction.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

        user.setId(userDTO.getId());
        user.setUserLoginId(userDTO.getUserLoginId());
        user.setUserType(nomal);
        user.setPass(bCryptPasswordEncoder.encode(userDTO.getPass()));
        user.setBirthDate(userDTO.getBirthDate());
        user.setGrade(bronze);

        userRepository.save(user);
    }

    public void updatePassword(String username, String currentPassword, String newPassword) {
        User user = userRepository.findByUserLoginId(username)
                .orElseThrow(() -> new UsernameNotFoundException("유저를 찾을 수 없습니다."));

        if (!bCryptPasswordEncoder.matches(currentPassword, user.getPass())) {
            throw new IllegalArgumentException("현재 비밀번호가 올바르지 않습니다.");
        }

        user.setPass(bCryptPasswordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public void deleteAccount(String username, String password) {
        User user = userRepository.findByUserLoginId(username)
                .orElseThrow(() -> new UsernameNotFoundException("유저를 찾을 수 없습니다."));

        if (!bCryptPasswordEncoder.matches(password, user.getPass())) {
            throw new IllegalArgumentException("비밀번호가 올바르지 않습니다.");
        }

        userRepository.delete(user);
    }

    public UserDTO getUserByLoginId(String userLoginId) {
        User user = userRepository.findByUserLoginId(userLoginId)
                .orElseThrow(() -> new RuntimeException("유저가 없습니다"));
        // 엔티티 -> DTO 변환해서 리턴
        UserDTO userDTO = UserDTO.fromUser(user);

        return userDTO;
    }

    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("유저가 없습니다"));
        return UserDTO.fromUser(user);
    }
}
