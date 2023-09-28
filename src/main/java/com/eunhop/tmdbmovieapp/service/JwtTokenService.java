package com.eunhop.tmdbmovieapp.service;

import com.eunhop.tmdbmovieapp.domain.JwtToken;
import com.eunhop.tmdbmovieapp.domain.User;
import com.eunhop.tmdbmovieapp.repository.JwtTokenRepository;
import com.eunhop.tmdbmovieapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class JwtTokenService {
  private final JwtTokenRepository jwtTokenRepository;
  private final UserRepository userRepository;

  public void newToken(String accessToken,String refreshToken, String Email) {
    JwtToken jwtToken = new JwtToken(accessToken, refreshToken);
    Optional<User> user = userRepository.findByEmail(Email);
    if(user.isPresent()) {
      jwtToken.setUser(user.get());
      jwtTokenRepository.save(jwtToken);
    }
  }

  public JwtToken findByAccessToken(String accessToken) {
    return jwtTokenRepository.findByAccessToken(accessToken);
  }

  public JwtToken findByRefreshToken(String refreshToken) {
    return jwtTokenRepository.findByRefreshToken(refreshToken);
  }

  public void deleteToken(String refreshToken) {
    JwtToken jwtToken = jwtTokenRepository.findByRefreshToken(refreshToken);
    if(jwtToken != null) {
      jwtTokenRepository.delete(jwtToken);
    }
  }

  public JwtToken updateNewAccessToken(String newAccessToken, String refreshToken) {
    JwtToken jwtToken = jwtTokenRepository.findByRefreshToken(refreshToken);
    if(jwtToken != null) {
      jwtToken.setAccessToken(newAccessToken);
      return jwtTokenRepository.save(jwtToken);
    }
    throw new RuntimeException("업데이트 오류");
  }
}
