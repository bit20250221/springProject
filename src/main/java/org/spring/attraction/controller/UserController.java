package org.spring.attraction.controller;

import org.spring.attraction.dto.ReservationDto;
import org.spring.attraction.dto.user.CustomUserDetails;
import org.spring.attraction.dto.user.UserDTO;
import org.spring.attraction.service.ReservationService;
import org.spring.attraction.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private ReservationService reservationService;

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @GetMapping("/myPage")
    public String myPage(Principal principal, Model model) {
        // Principal에서 로그인된 사용자의 username(아이디) 가져오기
        String userLoginId = principal.getName();
        // 유저 정보를 서비스 계층에서 조회
        UserDTO userDTO = userService.getUserByLoginId(userLoginId);
        // View로 유저 정보, 예약 정보 전달
        List<ReservationDto> reservationDto = reservationService.getReservationsByLoginId(userLoginId);
        model.addAttribute("user", userDTO);
        model.addAttribute("reservations", reservationDto);
        return "myPage";
    }

    @GetMapping("/register")
    public String regist() {
        return "register";
    }

    @PostMapping("/registerProc")
    public String register(@ModelAttribute UserDTO userDTO) {

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

    @PostMapping("/reservation/delete/{id}")
    public String cancelReservation(@PathVariable Long id, Principal principal) {
        // 현재 로그인된 사용자의 ID를 가져오기
        String userLoginId = principal.getName();

        // 예약 취소 처리
        reservationService.cancelReservation(id, userLoginId);

        // 처리 후 마이 페이지로 리다이렉트
        return "redirect:/user/myPage";
    }
}
