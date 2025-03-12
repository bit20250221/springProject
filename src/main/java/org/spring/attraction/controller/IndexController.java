package org.spring.attraction.controller;


import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
    @GetMapping("/")
    public String main(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (userDetails != null) {  // 로그인한 사용자가 있을 경우
            model.addAttribute("userName", userDetails.getUsername());
        }
        return "index";
    }

}
