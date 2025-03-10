package org.spring.attraction.controller;

import lombok.extern.slf4j.Slf4j;
import org.spring.attraction.dto.user.UserDTO;
import org.spring.attraction.service.AdminService;
import org.spring.attraction.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Slf4j
@Controller
public class AdminController {

    private final AdminService adminService;
    private final UserService userService;

    public AdminController(AdminService adminService, UserService userService) {
        this.adminService = adminService;
        this.userService = userService;
    }

    @GetMapping("/admin")
    public String adminForm(){
        return "admin";
    }

    // 전체 유저 목록 조회
    @GetMapping("/admin/user/")
    public String userListForm(Model model){
        List<UserDTO> viewUserDTOList = adminService.getAllUsers();
        model.addAttribute("userList", viewUserDTOList);
        return "userList";
    }

    // 특정 유저 조회
    @GetMapping("/admin/user/{userLoginId}")
    public String findById(@PathVariable String userLoginId, Model model){
        UserDTO userDTO = userService.getUserByLoginId(userLoginId);

        model.addAttribute("user", userDTO);
        return "userDetail";
    }

    @GetMapping("/admin/user/update/{userLoginId}")
    public String updateForm (@PathVariable String userLoginId, Model model){
        UserDTO userDTO = userService.getUserByLoginId(userLoginId);

        model.addAttribute("user", userDTO);
        return "userUpdate";
    }

    // 특정 유저 수정
    @PostMapping("/admin/user/update")
    public String updateUser(@ModelAttribute("user") UserDTO userDTO, Model model){
        UserDTO user = adminService.updateUser(userDTO);
        model.addAttribute("user", user);
        return "redirect:/admin/user";
    }
}