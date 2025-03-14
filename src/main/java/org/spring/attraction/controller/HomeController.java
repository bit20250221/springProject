package org.spring.attraction.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping({"index"})
    public String homeForm() {
        return "index";
    }
}
