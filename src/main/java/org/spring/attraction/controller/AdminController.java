package org.spring.attraction.controller;

import org.spring.attraction.dto.user.ViewUserDTO;
import org.spring.attraction.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/admin")
    public String adminForm(){
        return "admin";
    }

    @GetMapping("/admin/userList")
    public String userListForm(Model model){
        List<ViewUserDTO> viewUserDTOList = adminService.getAllUsers();
        model.addAttribute("userList", viewUserDTOList);
        return "userList";
    }
}
