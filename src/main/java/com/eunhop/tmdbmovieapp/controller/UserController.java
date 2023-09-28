package com.eunhop.tmdbmovieapp.controller;

import com.eunhop.tmdbmovieapp.domain.Roles;
import com.eunhop.tmdbmovieapp.dto.RegisterUserDto;
import com.eunhop.tmdbmovieapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * 회원가입 Controller
 */
@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * @return 회원가입 페이지 리소스
     */
    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/signup")
    public String signup(
            @ModelAttribute RegisterUserDto userDto
    ) {
        userService.signUp(userDto.getEmail(), userDto.getPassword(), userDto.getNickname(), Roles.USER, true);
        // 회원가입 후 로그인 페이지로 이동
        return "index";
    }
}
