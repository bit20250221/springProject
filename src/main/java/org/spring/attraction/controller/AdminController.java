package org.spring.attraction.controller;

import lombok.extern.slf4j.Slf4j;
import org.spring.attraction.dto.user.UserDTO;
import org.spring.attraction.service.AdminService;
import org.spring.attraction.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    // 전체 유저 목록 조회 (페이징 추가)
    @GetMapping("/user/")
    public String userListForm(@PageableDefault(page = 1) Pageable pageable, Model model) {
        Page<UserDTO> userList = adminService.paging(pageable);  // 페이징 처리된 유저 리스트 조회
        int blockLimit = 3;
        int startPage = (((int)(Math.ceil((double)pageable.getPageNumber() / blockLimit))) - 1) * blockLimit;
        int endPage = (startPage + blockLimit - 1) < userList.getTotalPages() ? startPage + blockLimit : userList.getTotalPages();

        model.addAttribute("userList", userList);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        return "userList";  // userList.html에서 페이징 처리를 표시하도록 함
    }

    // 특정 유저 조회
    @GetMapping("/user/{id}")
    public String findById(@PathVariable Long id, Model model) {
        UserDTO userDTO = userService.getUserById(id);  // ID로 조회
        model.addAttribute("user", userDTO);
        return "userDetail";
    }


    // 수정 페이지로 이동 (ID 사용)
    @GetMapping("/user/update/{id}")
    public String updateForm(@PathVariable Long id, Model model) {
        UserDTO userDTO = userService.getUserById(id);  // ID로 조회
        model.addAttribute("user", userDTO);
        return "userUpdate";
    }

    // 수정 처리 (ID 사용)
    @PostMapping("/user/update")
    public String updateUser(Long id, @ModelAttribute("user") UserDTO userDTO) {
        userDTO.setId(id);  // ID 설정
        adminService.updateUser(userDTO);
        return "redirect:/admin/user/" + id;
    }

    // 삭제 처리 (ID 사용)
    @GetMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        adminService.deleteUser(id);  // ID로 삭제
        return "redirect:/admin/user/";
    }
}