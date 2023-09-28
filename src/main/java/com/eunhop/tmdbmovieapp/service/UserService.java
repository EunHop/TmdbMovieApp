package com.eunhop.tmdbmovieapp.service;

import com.eunhop.tmdbmovieapp.domain.Roles;
import com.eunhop.tmdbmovieapp.domain.User;
import com.eunhop.tmdbmovieapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  /**
   * 유저 등록
   *
   * @param email email
   * @param password password
   * @param nickname nickname
   * @param role role
   */
  public User signUp(
      String email,
      String password,
      String nickname,
      Roles role,
      boolean enabled
  ) {
    if (userRepository.findByEmail(email).isPresent()) {
      throw new RuntimeException();
    }
    return userRepository.save(new User(email, passwordEncoder.encode(password),nickname, role, enabled));
  }

}
