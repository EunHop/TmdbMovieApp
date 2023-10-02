package com.eunhop.tmdbmovieapp.controller;

import com.eunhop.tmdbmovieapp.domain.Roles;
import com.eunhop.tmdbmovieapp.domain.User;
import com.eunhop.tmdbmovieapp.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

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
    public String signup(@ModelAttribute User user, HttpServletResponse response) {
        userService.registration(user);
        userService.login(response, user);
        // 회원가입 후 로그인 페이지로 이동
        return "index";
    }

}
