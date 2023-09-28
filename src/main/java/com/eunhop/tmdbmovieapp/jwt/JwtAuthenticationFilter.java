package com.eunhop.tmdbmovieapp.jwt;

import com.eunhop.tmdbmovieapp.domain.JwtToken;
import com.eunhop.tmdbmovieapp.domain.User;
import com.eunhop.tmdbmovieapp.repository.JwtTokenRepository;
import com.eunhop.tmdbmovieapp.service.JwtTokenService;
import com.eunhop.tmdbmovieapp.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.ArrayList;

/**
 * JWT를 이용한 로그인 인증
 */
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private final AuthenticationManager authenticationManager;
  private final JwtTokenService jwtTokenService;

  public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtTokenService jwtTokenService) {
    super(authenticationManager);
    this.authenticationManager = authenticationManager;
    this.jwtTokenService = jwtTokenService;
  }

  /**
   * 로그인 인증 시도
   */
  public Authentication attemptAuthentication(
      HttpServletRequest request,
      HttpServletResponse response
  ) throws AuthenticationException {
    // 로그인할 때 입력한 username과 password를 가지고 authenticationToken를 생성한다.
    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
        request.getParameter("email"),
        request.getParameter("password"),
        new ArrayList<>()
    );
    return authenticationManager.authenticate(authenticationToken);
  }

  /**
   * 인증에 성공했을 때 사용
   * JWT accessToken과 refreshToken을 생성해서 쿠키에 넣는다.
   */
  protected void successfulAuthentication(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain chain,
      Authentication authResult
  ) throws IOException {
    User user = (User) authResult.getPrincipal();
    String accessToken = JwtUtils.createAccessToken(user);
    String refreshToken = JwtUtils.createRefreshToken(user);
    jwtTokenService.newToken(accessToken, refreshToken, user.getEmail());
    // 쿠키 생성
    Cookie cookie1 = new Cookie(JwtProperties.ACCESS_TOKEN_COOKIE_NAME.getDescription(), accessToken);
    cookie1.setMaxAge(Integer.parseInt(JwtProperties.ACCESS_EXPIRATION_TIME.getDescription())); // 쿠키의 만료시간 설정
    cookie1.setSecure(true);
    cookie1.setHttpOnly(true);
    cookie1.setPath("/");
    Cookie cookie2 = new Cookie(JwtProperties.REFRESH_TOKEN_COOKIE_NAME.getDescription(), refreshToken);
    cookie2.setMaxAge(Integer.parseInt(JwtProperties.REFRESH_EXPIRATION_TIME.getDescription())); // 쿠키의 만료시간 설정
    cookie2.setSecure(true);
    cookie2.setHttpOnly(true);
    cookie2.setPath("/");
    response.addCookie(cookie1);
    response.addCookie(cookie2);
    response.sendRedirect("/");
  }

  protected void unsuccessfulAuthentication(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException failed
  ) throws IOException {
    response.sendRedirect("/login");
  }


}