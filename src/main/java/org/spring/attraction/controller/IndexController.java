package org.spring.attraction.controller;


import lombok.RequiredArgsConstructor;
import org.spring.attraction.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class IndexController {
    private final UserService userService;

    @GetMapping("/")
    public String main(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        model.addAttribute("userRole", userService.getUserRole(userDetails));
        return "index";
    }

}
