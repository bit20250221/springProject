package org.spring.attraction.controller;

import org.spring.attraction.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    private final UserService userService;

    public MainController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/main")
    public String mainForm(Model model) {
        String auth = userService.getCurrentUserAuthority();
        model.addAttribute("auth", auth);
        return "main";
    }
}