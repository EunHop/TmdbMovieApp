package com.eunhop.tmdbmovieapp.config;

import com.eunhop.tmdbmovieapp.domain.Roles;
import com.eunhop.tmdbmovieapp.jwt.JwtAuthenticationFilter;
import com.eunhop.tmdbmovieapp.jwt.JwtAuthorizationFilter;
import com.eunhop.tmdbmovieapp.jwt.JwtProperties;
import com.eunhop.tmdbmovieapp.repository.UserRepository;
import com.eunhop.tmdbmovieapp.service.JwtTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final UserRepository userRepository;
  private final JwtTokenService jwtTokenService;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.
        // basic authentication
            httpBasic(AbstractHttpConfigurer::disable) // basic authentication filter 비활성화
        // csrf
        .csrf(AbstractHttpConfigurer::disable)
        // remember-me
        .rememberMe(AbstractHttpConfigurer::disable)
        // stateless
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        // jwt filter
        .apply(new MyCustomDsl());
    // authorization
    http.authorizeHttpRequests(authorizeRequests -> authorizeRequests
            .requestMatchers("/", "/signup").permitAll()
            .requestMatchers(HttpMethod.GET, "/movie").permitAll()
            .requestMatchers(HttpMethod.HEAD, "/movie").permitAll()
            .requestMatchers(HttpMethod.GET, "/notice").permitAll()
            .requestMatchers(HttpMethod.HEAD, "/notice").permitAll()
            .requestMatchers(HttpMethod.POST, "/movie").hasRole(Roles.USER.name())
            .requestMatchers(HttpMethod.POST, "/movie/detail").hasRole(Roles.USER.name())
            .requestMatchers(HttpMethod.DELETE, "/movie/detail").hasRole(Roles.USER.name())
            .requestMatchers(HttpMethod.POST, "/notice").hasRole(Roles.ADMIN.name())
            .requestMatchers(HttpMethod.DELETE, "/notice").hasRole(Roles.ADMIN.name())
            .anyRequest().authenticated()
        )
        // login
        .formLogin(login -> login
            .loginPage("/login")
            .defaultSuccessUrl("/")
            .permitAll() //모두 허용
        )
        // logout
        .logout(logout -> logout
            .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
            .logoutSuccessUrl("/login")
            .invalidateHttpSession(true)
            .deleteCookies(JwtProperties.ACCESS_TOKEN_COOKIE_NAME.getDescription())
            .deleteCookies(JwtProperties.REFRESH_TOKEN_COOKIE_NAME.getDescription())
        );
    return http.build();
  }

  @Bean
  public WebSecurityCustomizer configure() {
    // 정적 리소스 spring security 대상에서 제외
    //        web.ignoring().antMatchers("/images/**", "/css/**"); // 아래 코드와 같은 코드입니다.
    return web -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
  }

  @Bean
  AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  public class MyCustomDsl extends AbstractHttpConfigurer<MyCustomDsl, HttpSecurity> {
    @Override
    public void configure(HttpSecurity http) throws Exception {
      AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
      http
          .addFilterBefore(new JwtAuthenticationFilter(authenticationManager, jwtTokenService), UsernamePasswordAuthenticationFilter.class)
          .addFilterBefore(new JwtAuthorizationFilter(userRepository, jwtTokenService), BasicAuthenticationFilter.class);

    }
  }
}


