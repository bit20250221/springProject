package org.spring.attraction.controller;

import jakarta.servlet.http.HttpSession;
import org.spring.attraction.dto.user.UserDTO;
import org.spring.attraction.entity.User;
import org.spring.attraction.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

//    @PostMapping("/loginProc")
//    public String login(@ModelAttribute UserDTO userDTO, HttpSession session) {
//
//        User user = userService.login(userDTO);
//
//        System.out.println("Test : " + userDTO.getUserLoginId() + " " + userDTO.getUserType());
//
//        if(user != null){ // 로그인 성공시
//            session.setAttribute("userLoginId", userDTO.getUserLoginId());
//            session.setAttribute("userType", userDTO.getUserType());
//            System.out.println(session.getAttribute("userLoginId") + " " + session.getAttribute("userType"));
//            System.out.println("로그인 성공");
//            return "main";
//        }
//        else{
//            System.out.println("로그인 실패");
//            return "redirect:/login";
//        }
//    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/register")
    public String regist() {
        return "register";
    }

    @PostMapping("/registerProc")
    public String register(@ModelAttribute UserDTO userDTO) {

        userService.registerProcess(userDTO);
        return "redirect:/login";
    }
}
