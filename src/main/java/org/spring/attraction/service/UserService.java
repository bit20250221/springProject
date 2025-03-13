package org.spring.attraction.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.spring.attraction.dto.UserDto;
import org.spring.attraction.entity.User;
import org.spring.attraction.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

import static org.spring.attraction.ENUM.Grade.bronze;
import static org.spring.attraction.ENUM.UserType.manager;
import static org.spring.attraction.ENUM.UserType.nomal;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void registerProcess(UserDto userDto) {

        boolean isUser = userRepository.existsByUserLoginId(userDto.getUserLoginId());
        if(isUser) {
            return;
        }

        User user = new User();
        System.out.println(userDto);
        user.setId(userDto.getId());
        user.setUserLoginId(userDto.getUserLoginId());
        user.setUserType(nomal);
        user.setPass(bCryptPasswordEncoder.encode(userDto.getPass()));
        user.setBirthDate(userDto.getBirthDate());
        user.setGrade(bronze);
        System.out.println(user.getUserLoginId() + " " + user.getPass());
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

    public UserDto getUserByLoginId(String userLoginId) {
        User user = userRepository.findByUserLoginId(userLoginId)
                .orElseThrow(() -> new RuntimeException("유저가 없습니다"));
        // 엔티티 -> DTO 변환해서 리턴

        UserDto userDto = UserDto.fromUser(user);



        return userDto;
    }

    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("유저가 없습니다"));
        return UserDto.fromUser(user);
    }

    public String getUserRole(UserDetails userDetails) {
        if (userDetails != null) {  // 로그인한 사용자가 있을 경우
            String userRole = userDetails.getAuthorities().stream()
                    .map(grantedAuthority -> grantedAuthority.getAuthority())
                    .collect(Collectors.joining(", "));
            return userRole;
        }
        return null;
    }

    public String getUserLoginId(UserDetails userDetails) {
        if (userDetails != null) {  // 로그인한 사용자가 있을 경우
            return userDetails.getUsername();
        }
        return null;
    }

    public Long getAttractionId(UserDetails userDetails) {
        if (userDetails != null) {  // 로그인한 사용자가 있을 경우
            String userRole = userDetails.getAuthorities().stream()
                    .map(grantedAuthority -> grantedAuthority.getAuthority())
                    .collect(Collectors.joining(", "));
            if(userRole.equals("attraction")){
                User user = userRepository.findByUserLoginId(userDetails.getUsername()).orElse(null);
                if(user != null){
                    if(user.getAttraction() != null){
                        return user.getAttraction().getId();
                    }
                }
            }
        }
        return null;
    }

    public User getUser(UserDetails userDetails) {
        if (userDetails != null) {
            String userLoginId = userDetails.getUsername();
            User user = userRepository.findByUserLoginId(userLoginId).orElse(null);
            if(user != null){
                return user;
            }
        }
        return null;
    }
}