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

  public void newToken(String accessToken,String refreshToken, String email) {
    JwtToken jwtToken = JwtToken.builder().accessToken(accessToken).refreshToken(refreshToken).build();
    Optional<User> user = userRepository.findByEmail(email);
    if(user.isPresent()) {
      jwtToken.setUser(user.get());
      jwtTokenRepository.save(jwtToken);
    }
  }

  public JwtToken findByAccessToken(String accessToken) {
    return jwtTokenRepository.findByAccessToken(accessToken);
  }

  public Optional<JwtToken> findByRefreshToken(String refreshToken) {
    return jwtTokenRepository.findByRefreshToken(refreshToken);
  }

  public void deleteByRefreshToken(String refreshToken) {
    Optional<JwtToken> jwtToken = jwtTokenRepository.findByRefreshToken(refreshToken);
    if(jwtToken.isPresent()) {
      jwtTokenRepository.delete(jwtToken.get());
    }
  }

  public void deleteByAccessToken(String accessToken) {
    JwtToken jwtToken = jwtTokenRepository.findByAccessToken(accessToken);
    if(jwtToken != null) {
      jwtTokenRepository.delete(jwtToken);
    }
  }

  public boolean dataAlreadyExist(String email) {
    Optional<User> user = userRepository.findByEmail(email);
    if(user.isPresent()) {
      Optional<JwtToken> jwtToken = jwtTokenRepository.findByUserId(user.get().getId());
      if(jwtToken.isPresent()) {
        jwtTokenRepository.delete(jwtToken.get());
        return true;
      }
      return false;
    }
    return false;
  }

  public boolean userIsPresent(String email) {
    Optional<User> user = userRepository.findByEmail(email);
    return user.isPresent();
  }

  public JwtToken updateNewAccessToken(String newAccessToken, String refreshToken) {
    Optional<JwtToken> jwtToken = jwtTokenRepository.findByRefreshToken(refreshToken);
    if(jwtToken.isPresent()) {
      jwtToken.get().setAccessToken(newAccessToken);
      return jwtTokenRepository.save(jwtToken.get());
    }
    throw new RuntimeException("업데이트 오류");
  }
}
