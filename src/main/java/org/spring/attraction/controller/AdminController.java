package org.spring.attraction.controller;

import lombok.extern.slf4j.Slf4j;
import org.spring.attraction.dto.UserDto;
import org.spring.attraction.service.AdminService;
import org.spring.attraction.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;
    private final UserService userService;

    public AdminController(AdminService adminService, UserService userService) {
        this.adminService = adminService;
        this.userService = userService;
    }

    @GetMapping("")
    public String adminForm(){
        return "admin";
    }

    // 전체 유저 목록 조회
    @GetMapping("/user/")
    public String userListForm(Model model){
        List<UserDto> viewUserDTOList = adminService.getAllUsers();
        model.addAttribute("userList", viewUserDTOList);
        return "userList";
    }

    // 특정 유저 조회
    @GetMapping("/user/{id}")
    public String findById(@PathVariable Long id, Model model) {
        UserDto userDto = userService.getUserById(id);  // ID로 조회
        model.addAttribute("user", userDto);
        return "userDetail";
    }


    // 수정 페이지로 이동 (ID 사용)
    @GetMapping("/user/update/{id}")
    public String updateForm(@PathVariable Long id, Model model) {
        UserDto userDto = userService.getUserById(id);  // ID로 조회
        model.addAttribute("user", userDto);
        return "userUpdate";
    }

    // 수정 처리 (ID 사용)
    @PostMapping("/user/update")
    public String updateUser(Long id, @ModelAttribute("user") UserDto userDto) {
        userDto.setId(id);  // ID 설정
        adminService.updateUser(userDto);
        return "redirect:/admin/user/" + id;
    }

    // 삭제 처리 (ID 사용)
    @GetMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        adminService.deleteUser(id);  // ID로 삭제
        return "redirect:/admin/user/";
    }
}