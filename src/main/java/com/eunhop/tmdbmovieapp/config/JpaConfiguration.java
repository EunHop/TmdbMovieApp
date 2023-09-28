package com.eunhop.tmdbmovieapp.config;

import com.eunhop.tmdbmovieapp.domain.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Configuration
@EnableJpaAuditing
public class JpaConfiguration {
//  @Bean
//  public AuditorAware<String> auditorProvider() {
//    return () -> Optional.ofNullable(SecurityContextHolder.getContext())
//        .map(SecurityContext::getAuthentication)
//        .filter(Authentication::isAuthenticated)
//        .map(Authentication::getPrincipal)
//        .map(User.class::cast)
//        .map(User::getUsername);
//  }
}
