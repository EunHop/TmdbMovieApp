package com.eunhop.tmdbmovieapp.config;

import com.eunhop.tmdbmovieapp.dto.security.PrincipalUser;
import com.eunhop.tmdbmovieapp.oauth2.OAuth2ClientRegistration;
import com.eunhop.tmdbmovieapp.domain.Roles;
import com.eunhop.tmdbmovieapp.domain.User;
import com.eunhop.tmdbmovieapp.jwt.JwtAuthenticationFilter;
import com.eunhop.tmdbmovieapp.jwt.JwtAuthorizationFilter;
import com.eunhop.tmdbmovieapp.jwt.JwtProperties;
import com.eunhop.tmdbmovieapp.oauth2.OAuth2SuccessHandler;
import com.eunhop.tmdbmovieapp.repository.UserRepository;
import com.eunhop.tmdbmovieapp.oauth2.CustomOAuth2UserService;
import com.eunhop.tmdbmovieapp.oauth2.GoogleOAuth2UserService;
import com.eunhop.tmdbmovieapp.service.CreateCookie;
import com.eunhop.tmdbmovieapp.service.JwtTokenService;
import com.eunhop.tmdbmovieapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final UserRepository userRepository;
  private final UserService userService;
  private final JwtTokenService jwtTokenService;
  private final CreateCookie createCookie;
  private final OAuth2ClientRegistration oAuth2ClientRegistration;


  @Bean
  public SecurityFilterChain filterChain(
      HttpSecurity http,
      CustomOAuth2UserService customOAuth2UserService,
      GoogleOAuth2UserService googleOAuth2UserService
  ) throws Exception {
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
            .requestMatchers("/", "/signup", "/login").permitAll()
            .requestMatchers(HttpMethod.GET, "/movie").permitAll()
            .requestMatchers(HttpMethod.HEAD, "/movie").permitAll()
            .requestMatchers(HttpMethod.GET, "/notice").permitAll()
            .requestMatchers(HttpMethod.HEAD, "/notice").permitAll()
            .requestMatchers(HttpMethod.POST, "/movie").authenticated()
            .requestMatchers(HttpMethod.POST, "/movie/detail").authenticated()
            .requestMatchers(HttpMethod.DELETE, "/movie/detail").authenticated()
            .requestMatchers(HttpMethod.POST, "/notice").hasRole(Roles.ADMIN.name())
            .requestMatchers(HttpMethod.DELETE, "/notice").hasRole(Roles.ADMIN.name())
            .anyRequest().permitAll() // 나중에 .authticated() 로 바꾸기
        )
        // login
        .formLogin(login -> login
            .loginPage("/login")
            .defaultSuccessUrl("/")
            .permitAll() //모두 허용
        )
        .oauth2Login(oauth2 -> oauth2
            .loginPage("/login")
            .defaultSuccessUrl("/")
            .successHandler(new OAuth2SuccessHandler(createCookie))
            .clientRegistrationRepository(clientRegistrationRepository())
            .authorizedClientService(authorizedClientService())
            .userInfoEndpoint(user -> user
                .oidcUserService(googleOAuth2UserService)  //google 인증, OpenID connect 1.0
                .userService(customOAuth2UserService)  //  kakao, naver 인증, OAuth2 통신
            )
            .permitAll()
        )
        // logout
        .logout(logout -> logout
            .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
            .logoutSuccessUrl("/")
            .invalidateHttpSession(true)
            .deleteCookies(JwtProperties.ACCESS_TOKEN_COOKIE_NAME.getDescription())
            .deleteCookies(JwtProperties.REFRESH_TOKEN_COOKIE_NAME.getDescription())
        )
    ;
    return http.build();
  }

  @Bean
  public OAuth2AuthorizedClientService authorizedClientService() {
    return new InMemoryOAuth2AuthorizedClientService(clientRegistrationRepository());
  }

  @Bean
  public ClientRegistrationRepository clientRegistrationRepository() {
    List<ClientRegistration> clientRegistrations = Arrays.asList(
        oAuth2ClientRegistration.googleClientRegistration(),
        oAuth2ClientRegistration.naverClientRegistration(),
        oAuth2ClientRegistration.kakaoClientRegistration()
    );
    return new InMemoryClientRegistrationRepository(clientRegistrations);
  }

  @Bean
  public WebSecurityCustomizer configure() {
    // 정적 리소스 spring security 대상에서 제외
    //        web.ignoring().antMatchers("/images/**", "/css/**"); // 아래 코드와 같은 코드입니다.
    return web -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
  }


  @Bean
  public UserDetailsService userDetailsService() {
    return email -> {
      Optional<User> user = userService.findByEmail(email);
      if (user.isEmpty()) {
        throw new UsernameNotFoundException(email);
      }
      return PrincipalUser.builder().user(user.get()).build();
    };
  }

  public class MyCustomDsl extends AbstractHttpConfigurer<MyCustomDsl, HttpSecurity> {
    @Override
    public void configure(HttpSecurity http) throws Exception {
      AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
      http
          .addFilterBefore(new JwtAuthenticationFilter(authenticationManager, createCookie), UsernamePasswordAuthenticationFilter.class)
          .addFilterBefore(new JwtAuthorizationFilter(userRepository, jwtTokenService), BasicAuthenticationFilter.class);
    }
  }
}


