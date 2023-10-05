package com.eunhop.tmdbmovieapp.jwt;

import com.eunhop.tmdbmovieapp.domain.JwtToken;
import com.eunhop.tmdbmovieapp.domain.Roles;
import com.eunhop.tmdbmovieapp.domain.User;
import com.eunhop.tmdbmovieapp.repository.UserRepository;
import com.eunhop.tmdbmovieapp.service.JwtTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
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
      // cookie 에서 JWT token을 가져옵니다.
    String jwtAccessToken = null;
    String jwtRefreshToken = null;
    try {
      jwtAccessToken = Arrays.stream(request.getCookies())
          .filter(cookie -> cookie.getName().equals(JwtProperties.ACCESS_TOKEN.getDescription())).findFirst()
          .map(Cookie::getValue)
          .orElse(null);
      jwtRefreshToken = Arrays.stream(request.getCookies())
          .filter(cookie -> cookie.getName().equals(JwtProperties.REFRESH_TOKEN.getDescription())).findFirst()
          .map(Cookie::getValue)
          .orElse(null);
    } catch (Exception ignored) {}
    if (jwtAccessToken != null & jwtRefreshToken != null) {
      if (jwtRefreshToken.equals(jwtTokenService.findByAccessToken(jwtAccessToken).getRefreshToken())) {
        Authentication authentication = getUsernamePasswordAuthenticationToken(jwtAccessToken, jwtRefreshToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
      }
    } else if (jwtAccessToken == null & jwtRefreshToken != null) {
      Optional<JwtToken> refresh = jwtTokenService.findByRefreshToken(jwtRefreshToken);
      if(refresh.isPresent()) {
        if (JwtUtils.accessTokenNotExpired(refresh.get().getAccessToken())) {
          jwtTokenService.deleteByRefreshToken(jwtRefreshToken);
          cookieDelete(response);
        }
        else {
          String newJwtAccessToken = JwtUtils.createAccessToken(refresh.get().getUser().getEmail());
          jwtTokenService.updateNewAccessToken(newJwtAccessToken, jwtRefreshToken);
          Cookie cookie1 = new Cookie(JwtProperties.ACCESS_TOKEN.getDescription(), newJwtAccessToken);
          cookie1.setMaxAge(JwtProperties.ACCESS_TOKEN.getTime()); // 쿠키의 만료시간 설정
          cookie1.setSecure(true);
          cookie1.setHttpOnly(true);
          cookie1.setPath("/");
          response.addCookie(cookie1);
          Authentication authentication = getUsernamePasswordAuthenticationToken(newJwtAccessToken, jwtRefreshToken);
          SecurityContextHolder.getContext().setAuthentication(authentication);
        }
      }
    }
    // jwtAccessToken != null & jwtRefreshToken == null
    else if (jwtAccessToken != null) {
      jwtTokenService.deleteByAccessToken(jwtAccessToken);
      cookieDelete(response);
    }
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
            user.get().getEmail(), // principal
            user.get().getPassword(),
            List.of(new SimpleGrantedAuthority(Roles.USER.getValue()))
        );
      }
    }
    return null; // 유저가 없으면 NULL
  }

  private void cookieDelete(HttpServletResponse response) {
    Cookie cookie1 = new Cookie(JwtProperties.ACCESS_TOKEN.getDescription(), null);
    cookie1.setMaxAge(0);
    Cookie cookie2 = new Cookie(JwtProperties.REFRESH_TOKEN.getDescription(), null);
    cookie2.setMaxAge(0);
    response.addCookie(cookie1);
    response.addCookie(cookie2);
  }
}