package com.eunhop.tmdbmovieapp.repository;

import com.eunhop.tmdbmovieapp.domain.JwtToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JwtTokenRepository extends JpaRepository<JwtToken, Long> {
  JwtToken findByAccessToken(String accessToken);
  JwtToken findByRefreshToken(String refreshToken);
}
