package org.spring.attraction.controller;

import lombok.extern.slf4j.Slf4j;
import org.spring.attraction.ENUM.UserType;
import org.spring.attraction.dto.UserDto;
import org.spring.attraction.service.AdminService;
import org.spring.attraction.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String userListForm(@PageableDefault(page = 1) Pageable pageable, Model model) {
        Page<UserDto> userList = adminService.paging(pageable);  // 페이징 처리된 유저 리스트 조회
        int blockLimit = 3;
        int startPage = (((int)(Math.ceil((double)pageable.getPageNumber() / blockLimit))) - 1) * blockLimit;
        int endPage = (startPage + blockLimit - 1) < userList.getTotalPages() ? startPage + blockLimit : userList.getTotalPages();

        model.addAttribute("userList", userList);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        return "/admin/userList";
    }

    // 특정 유저 조회
    @GetMapping("/user/{id}")
    public String findById(@PathVariable Long id, Model model) {
        UserDto userDto = userService.getUserById(id);  // ID로 조회
        model.addAttribute("user", userDto);
        return "/admin/userDetail";
    }

    // 특정 유저 생성 (관리자 생성)
    @GetMapping("/user/save")
    public String saveForm(){
        return "/admin/save";
    }

    @PostMapping("/user/save")
    public String createUser(@ModelAttribute UserDto userDto,
                             @RequestParam("userType") UserType userType,
                             RedirectAttributes redirectAttributes) {
        try {
            adminService.createUser(userDto, userType);
            redirectAttributes.addFlashAttribute("message", "유저가 성공적으로 생성되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "유저 생성 중 문제가 발생했습니다: " + e.getMessage());
        }
        return "redirect:/admin/user/";
    }

    // 수정 페이지로 이동 (ID 사용)
    @GetMapping("/user/update/{id}")
    public String updateForm(@PathVariable Long id, Model model) {
        UserDto userDto = userService.getUserById(id);  // ID로 조회
        model.addAttribute("user", userDto);
        return "/admin/userUpdate";
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