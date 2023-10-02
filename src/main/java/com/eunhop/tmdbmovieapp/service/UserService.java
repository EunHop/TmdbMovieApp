package com.eunhop.tmdbmovieapp.service;

import com.eunhop.tmdbmovieapp.domain.Roles;
import com.eunhop.tmdbmovieapp.domain.User;
import com.eunhop.tmdbmovieapp.jwt.JwtProperties;
import com.eunhop.tmdbmovieapp.jwt.JwtUtils;
import com.eunhop.tmdbmovieapp.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final CreateCookie createCookie;

  public void registration(User user) {
    if(userRepository.findByEmail(user.getEmail()).isPresent()) {
      throw new RuntimeException("이메일이 이미 존재합니다.");
    }
     userRepository.save(
         User.builder()
         .email(user.getEmail())
         .password(passwordEncoder.encode(user.getPassword()))
         .name(user.getName())
         .role(Roles.USER)
         .enabled(true)
         .build());
  }

  public void login(HttpServletResponse response, User user) {
    createCookie.createCookieUsingUser(response, user);

    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
        user.getEmail(),
        user.getPassword(),
        List.of(new SimpleGrantedAuthority(Roles.USER.getValue()))
    );
    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
  }

  public Optional<User> findByEmail(String email) {
    return userRepository.findByEmail(email);
  }

}
