package com.eunhop.tmdbmovieapp.controller;

import com.eunhop.tmdbmovieapp.domain.User;
import com.eunhop.tmdbmovieapp.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * 회원가입 Controller
 */
@Controller
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/signup")
    public String signup(@ModelAttribute User user, HttpServletResponse response, Model model) {
        if(userService.alreadySigned(user)) {
            model.addAttribute("msg", "이미 회원가입된 이메일 입니다.");
            return "signup";
        } else {
            userService.registration(user);
            userService.login(response, user);
            // 회원가입 후 자동 로그인
            return "redirect:/";
        }
    }
}
