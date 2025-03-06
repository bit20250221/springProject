package org.spring.attraction.service;

import org.spring.attraction.dto.user.UserDTO;
import org.spring.attraction.entity.User;
import org.spring.attraction.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import static org.spring.attraction.ENUM.Grade.bronze;
import static org.spring.attraction.ENUM.UserType.manager;
import static org.spring.attraction.ENUM.UserType.nomal;

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
        System.out.println(userDTO.getId());
        System.out.println(userDTO.getUserLoginId());
        System.out.println(userDTO.getPass());
        System.out.println(userDTO.getBirthDate());

        user.setId(userDTO.getId());
        user.setUserLoginId(userDTO.getUserLoginId());
        user.setUserType(nomal);
        user.setPass(bCryptPasswordEncoder.encode(userDTO.getPass()));
        user.setBirthDate(userDTO.getBirthDate());
        user.setGrade(bronze);

        userRepository.save(user);
    }

//    public User login(UserDTO userDTO){
//        System.out.println("로그인 시도: " + userDTO.getUserLoginId() + " / " + userDTO.getPass());
//
//        User byUserLoginId = userRepository.findByUserLoginId(userDTO.getUserLoginId());
//
//        if(byUserLoginId != null){
//            User user = byUserLoginId;
//            System.out.println("DB에서 찾은 유저: " + user.getUserLoginId() + " / " + user.getPass());
//
//            if(bCryptPasswordEncoder.matches(userDTO.getPass(), user.getPass())){
//                System.out.println("비밀번호 일치");
//                return user;
//            } else {
//                System.out.println("비밀번호 불일치");
//                return null;
//            }
//        } else {
//            System.out.println("해당 아이디가 존재하지 않음");
//            return null;
//        }
//    }

}
