package com.eunhop.tmdbmovieapp.repository;

import com.eunhop.tmdbmovieapp.domain.JwtToken;
import com.eunhop.tmdbmovieapp.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JwtTokenRepository extends JpaRepository<JwtToken, Long> {
  JwtToken findByAccessToken(String accessToken);
  Optional<JwtToken> findByRefreshToken(String refreshToken);
  Optional<JwtToken> findByUserId(Long id);
}
