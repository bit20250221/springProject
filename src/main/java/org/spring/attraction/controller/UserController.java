package org.spring.attraction.controller;

import org.spring.attraction.dto.CustomUserDetails;
import org.spring.attraction.dto.UserDto;
import org.spring.attraction.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String loginForm() {
        return "/user/login";
    }

    @GetMapping("/myPage")
    public String myPage(Principal principal, Model model) {
        // Principal에서 로그인된 사용자의 username(아이디) 가져오기
        String userLoginId = principal.getName();

        // 유저 정보를 서비스 계층에서 조회
        UserDto userDTO = userService.getUserByLoginId(userLoginId);

        // View로 전달
        model.addAttribute("user", userDTO);
        return "myPage";
    }

    @GetMapping("/save")
    public String save() {
        return "/user/save";
    }

    @PostMapping("/save")
    public String register(@ModelAttribute UserDto userDTO) {

        userService.registerProcess(userDTO);
        return "redirect:/login";
    }

    @PostMapping("/update")
    public String updatePassword(
            @RequestParam String currentPassword,
            @RequestParam String newPassword,
            @RequestParam String confirmNewPassword,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        if (!newPassword.equals(confirmNewPassword)) {
            throw new IllegalArgumentException("새 비밀번호가 일치하지 않습니다.");
        }

        // 비밀번호 변경 처리
        userService.updatePassword(userDetails.getUsername(), currentPassword, newPassword);

        // 비밀번호 변경 후, Security의 기본 로그아웃 처리로 리다이렉트
        return "redirect:/user/logout";
    }

    @PostMapping("/delete")
    public String deleteAccount(
            @RequestParam String password,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        // 회원 탈퇴 처리
        userService.deleteAccount(userDetails.getUsername(), password);

        // 탈퇴 후, Security의 기본 로그아웃 처리로 리다이렉트
        return "redirect:/user/logout";
    }
}