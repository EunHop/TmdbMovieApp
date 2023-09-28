package com.eunhop.tmdbmovieapp.jwt;

import com.eunhop.tmdbmovieapp.domain.User;
import com.eunhop.tmdbmovieapp.repository.JwtTokenRepository;
import com.eunhop.tmdbmovieapp.repository.UserRepository;
import com.eunhop.tmdbmovieapp.service.JwtTokenService;
import com.eunhop.tmdbmovieapp.service.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwt;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

/**
 * JWT를 이용한 Authorization(헤더)
 */
@Slf4j
public class JwtAuthorizationFilter extends OncePerRequestFilter {

  private final JwtTokenService jwtTokenService;
  private final UserRepository userRepository;

  public JwtAuthorizationFilter(UserRepository userRepository, JwtTokenService jwtTokenService) {
    this.userRepository = userRepository;
    this.jwtTokenService = jwtTokenService;
  }

  protected void doFilterInternal(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain chain
  ) throws IOException, ServletException {
    String accessToken = null;
    String refreshToken = null;
    String userEmail = null;
    try {
      // cookie 에서 JWT token을 가져옵니다.
      accessToken = Arrays.stream(request.getCookies())
          .filter(cookie -> cookie.getName().equals(JwtProperties.ACCESS_TOKEN_COOKIE_NAME.getDescription())).findFirst()
          .map(Cookie::getValue)
          .orElse(null);
      refreshToken = Arrays.stream(request.getCookies())
          .filter(cookie -> cookie.getName().equals(JwtProperties.REFRESH_TOKEN_COOKIE_NAME.getDescription())).findFirst()
          .map(Cookie::getValue)
          .orElse(null);
    } catch (Exception ignored) {
    }
    if (accessToken != null & refreshToken != null) {
      if (refreshToken.equals(jwtTokenService.findByAccessToken(accessToken).getRefreshToken())) {
        Authentication authentication = getUsernamePasswordAuthenticationToken(accessToken, refreshToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
      }
    } else if (accessToken == null & refreshToken != null) {
      if (JwtUtils.accessTokenNotExpired(jwtTokenService.findByRefreshToken(refreshToken).getAccessToken())) {
        jwtTokenService.deleteToken(refreshToken);
        Cookie cookie1 = new Cookie(JwtProperties.ACCESS_TOKEN_COOKIE_NAME.getDescription(), null);
        cookie1.setMaxAge(0);
        Cookie cookie2 = new Cookie(JwtProperties.REFRESH_TOKEN_COOKIE_NAME.getDescription(), null);
        cookie2.setMaxAge(0);
        response.addCookie(cookie1);
        response.addCookie(cookie2);

      } else {
        String newAccessToken = JwtUtils.createAccessToken(jwtTokenService.findByRefreshToken(refreshToken).getUser());
        jwtTokenService.updateNewAccessToken(newAccessToken, refreshToken);
        Cookie cookie1 = new Cookie(JwtProperties.ACCESS_TOKEN_COOKIE_NAME.getDescription(), newAccessToken);
        cookie1.setMaxAge(Integer.parseInt(JwtProperties.ACCESS_TOKEN_COOKIE_NAME.getDescription())); // 쿠키의 만료시간 설정
        cookie1.setSecure(true);
        cookie1.setHttpOnly(true);
        cookie1.setPath("/");
        response.addCookie(cookie1);
        Authentication authentication = getUsernamePasswordAuthenticationToken(accessToken, newAccessToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
      }
    }
    jwtTokenService.deleteToken(refreshToken);
    Cookie cookie1 = new Cookie(JwtProperties.ACCESS_TOKEN_COOKIE_NAME.getDescription(), null);
    cookie1.setMaxAge(0);
    Cookie cookie2 = new Cookie(JwtProperties.REFRESH_TOKEN_COOKIE_NAME.getDescription(), null);
    cookie2.setMaxAge(0);
    response.addCookie(cookie1);
    response.addCookie(cookie2);
    chain.doFilter(request, response);
  }

  /**
   * JWT 토큰으로 User를 찾아서 UsernamePasswordAuthenticationToken를 만들어서 반환한다.
   * User가 없다면 null
   */
  private Authentication getUsernamePasswordAuthenticationToken(String accessToken, String refreshToken) {
    String email1 = JwtUtils.getUserEmail(accessToken);
    String email2 = JwtUtils.getUserEmail(refreshToken);
    if (email1 != null & email2 != null & Objects.equals(email1, email2)) {
      Optional<User> user = userRepository.findByEmail(email1); // 유저를 이메일로 찾습니다.
      if(user.isPresent()) {
        return new UsernamePasswordAuthenticationToken(
            user.get(), // principal
            user.get().getPassword(),
            user.get().getAuthorities());
      }
    }
    return null; // 유저가 없으면 NULL
  }
}