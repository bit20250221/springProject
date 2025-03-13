package org.spring.attraction.controller;

import org.spring.attraction.dto.CustomUserDetails;
import org.spring.attraction.dto.UserDto;
import org.spring.attraction.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String loginForm(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        model.addAttribute("userRole", userService.getUserRole(userDetails));
        if (userDetails != null) {  // 로그인한 사용자가 있을 경우
            String userRole = userDetails.getAuthorities().stream()
                    .map(grantedAuthority -> grantedAuthority.getAuthority())
                    .collect(Collectors.joining(", "));
            model.addAttribute("userRole", userRole);

        }
        return "/user/login";
    }

    @GetMapping("/myPage")
    public String myPage(Principal principal, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        model.addAttribute("userRole", userService.getUserRole(userDetails));
        // Principal에서 로그인된 사용자의 username(아이디) 가져오기
        String userLoginId = principal.getName();

        // 유저 정보를 서비스 계층에서 조회
        UserDto userDTO = userService.getUserByLoginId(userLoginId);

        // View로 전달
        model.addAttribute("user", userDTO);
        return "myPage";
    }

    @GetMapping("/save")
    public String save(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        model.addAttribute("userRole", userService.getUserRole(userDetails));
        return "/user/save";
    }

    @PostMapping("/save")
    public String register(@ModelAttribute UserDto userDTO, @AuthenticationPrincipal UserDetails userDetails, Model model) {
        model.addAttribute("userRole", userService.getUserRole(userDetails));
        userService.registerProcess(userDTO);
        return "redirect:/user/login";
    }

    @PostMapping("/update")
    public String updatePassword(
            @RequestParam String currentPassword,
            @RequestParam String newPassword,
            @RequestParam String confirmNewPassword,
            @AuthenticationPrincipal UserDetails userDetails, Model model) {
        model.addAttribute("userRole", userService.getUserRole(userDetails));
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
            @AuthenticationPrincipal UserDetails userDetails, Model model) {
        model.addAttribute("userRole", userService.getUserRole(userDetails));
        // 회원 탈퇴 처리
        userService.deleteAccount(userDetails.getUsername(), password);

        // 탈퇴 후, Security의 기본 로그아웃 처리로 리다이렉트
        return "redirect:/user/logout";
    }
}