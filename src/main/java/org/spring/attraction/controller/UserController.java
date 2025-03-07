package org.spring.attraction.controller;

import jakarta.servlet.http.HttpSession;
import org.spring.attraction.dto.user.CustomUserDetails;
import org.spring.attraction.dto.user.UserDTO;
import org.spring.attraction.dto.user.ViewUserDTO;
import org.spring.attraction.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @GetMapping("/myPage")
    public String myPage(Model model) {
        // 인증된 사용자 정보 가져오기
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();

        // ViewUserDTO를 View에서 사용
        ViewUserDTO userDTO = userDetails.getViewUserDTO();
        model.addAttribute("user", userDTO);

        return "myPage";
    }

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
