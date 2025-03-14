package org.spring.attraction.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/sdfsdfsdfsf")
@RequiredArgsConstructor
@Controller
@Log4j2
public class Home_controller {

    @GetMapping
    public String home(){
        return "index.html";
    }
}
