package org.spring.attraction.controller;

import org.spring.attraction.dto.user.CustomUserDetails;
import org.spring.attraction.dto.user.UserDTO;
import org.spring.attraction.dto.user.ViewUserDTO;
import org.spring.attraction.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @GetMapping("/myPage")
    public String myPage(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        // ViewUserDTO를 View에서 사용
        ViewUserDTO userDTO = userDetails.getViewUserDTO();

        model.addAttribute("user", userDTO);

        return "myPage";
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
