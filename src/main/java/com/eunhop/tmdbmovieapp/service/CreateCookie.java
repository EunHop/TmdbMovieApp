package com.eunhop.tmdbmovieapp.service;

import com.eunhop.tmdbmovieapp.domain.User;
import com.eunhop.tmdbmovieapp.jwt.JwtProperties;
import com.eunhop.tmdbmovieapp.jwt.JwtUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CreateCookie {
  private final JwtTokenService jwtTokenService;

  public void createCookieUsingAuthentication(HttpServletResponse response, Authentication authentication) {
    if(jwtTokenService.dataAlreadyExist(authentication.getName())) {
      log.info("jwtToken already exist so I removed it");
    } else {
      log.info("jwtToken not exist so I created it");
    }
    String jwtAccessToken = JwtUtils.createAccessToken(authentication.getName());
    String jwtRefreshToken = JwtUtils.createRefreshToken(authentication.getName());
    jwtTokenService.newToken(jwtAccessToken, jwtRefreshToken, authentication.getName());
    // 쿠키 생성
    createCookie(response, jwtAccessToken, jwtRefreshToken);
  }

  public void createCookieUsingUser(HttpServletResponse response, User user) {
    if(jwtTokenService.dataAlreadyExist(user.getEmail())) {
      log.info("jwtToken already exist so I removed it");
    } else {
      log.info("jwtToken not exist so I created it");
    }
    String jwtAccessToken = JwtUtils.createAccessToken(user.getEmail());
    String jwtRefreshToken = JwtUtils.createRefreshToken(user.getEmail());
    jwtTokenService.newToken(jwtAccessToken, jwtRefreshToken, user.getEmail());
    // 쿠키 생성
    createCookie(response, jwtAccessToken, jwtRefreshToken);
  }

  public void createCookie(HttpServletResponse response, String jwtAccessToken, String jwtRefreshToken) {
    Cookie cookie1 = new Cookie(JwtProperties.ACCESS_TOKEN.getDescription(), jwtAccessToken);
    cookie1.setMaxAge(JwtProperties.ACCESS_TOKEN.getTime()); // 쿠키의 만료시간 설정
    cookie1.setSecure(true);
    cookie1.setHttpOnly(true);
    cookie1.setPath("/");
    Cookie cookie2 = new Cookie(JwtProperties.REFRESH_TOKEN.getDescription(), jwtRefreshToken);
    cookie2.setMaxAge(JwtProperties.REFRESH_TOKEN.getTime()); // 쿠키의 만료시간 설정
    cookie2.setSecure(true);
    cookie2.setHttpOnly(true);
    cookie2.setPath("/");
    response.addCookie(cookie1);
    response.addCookie(cookie2);
  }
}
