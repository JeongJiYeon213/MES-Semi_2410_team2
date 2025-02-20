package org.zerock.b02.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.zerock.b02.domain.Admin;
import org.zerock.b02.service.AdminService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
@SessionAttributes("admin") // 세션에 "admin" 객체를 저장
@RequestMapping("/mes")
public class LoginController {

    private final AdminService adminService;

    // 로그인 페이지로 이동
    @GetMapping("/login")
    public String loginPage() {
        return "login"; // 로그인 HTML 페이지 (login.html)
    }

    // 로그인 처리
    @PostMapping("/login")
    public String login(@RequestParam("adminId") Long adminId,
                        @RequestParam("adminPassword") int adminPassword,
                        HttpSession session,
                        Model model) {

        // AdminService를 사용하여 아이디와 비밀번호로 인증
        Admin admin = adminService.login(adminId, adminPassword);


        if (admin != null) {
            session.setAttribute("adminName", admin.getAdminName());
            session.setAttribute("position", admin.getPosition());
            model.addAttribute("admin", admin); // 세션에 admin 객체 저장
            session.setAttribute("adminId", adminId);
            return "redirect:/mes/dashboard"; // 로그인 성공 시 대시보드로 리다이렉트
        } else {
            model.addAttribute("error", "아이디나 비밀번호가 잘못되었습니다.");
            return "login"; // 로그인 실패 시 로그인 페이지로 다시 돌아가기
        }
    }

    // 로그아웃 처리
    @GetMapping("/logout")
    public String logout(Model model) {
        model.asMap().remove("admin"); // 세션에서 admin 제거
        return "redirect:/mes/login"; // 로그아웃 후 로그인 페이지로 리다이렉트
    }
}
