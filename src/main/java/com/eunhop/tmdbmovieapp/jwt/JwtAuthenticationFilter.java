package com.eunhop.tmdbmovieapp.jwt;

import com.eunhop.tmdbmovieapp.domain.Roles;
import com.eunhop.tmdbmovieapp.dto.security.PrincipalUser;
import com.eunhop.tmdbmovieapp.service.CreateCookie;
import com.eunhop.tmdbmovieapp.service.JwtTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.List;

/**
 * JWT를 이용한 로그인 인증
 */
@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private final AuthenticationManager authenticationManager;
  private final CreateCookie createCookie;

  public JwtAuthenticationFilter(AuthenticationManager authenticationManager, CreateCookie createCookie) {
    super(authenticationManager);
    this.authenticationManager = authenticationManager;
    this.createCookie = createCookie;
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
        List.of(new SimpleGrantedAuthority(Roles.USER.getValue()))
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
    createCookie.createCookieUsingAuthentication(response, authResult);

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