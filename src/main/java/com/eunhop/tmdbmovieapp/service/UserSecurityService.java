package com.eunhop.tmdbmovieapp.service;

import com.eunhop.tmdbmovieapp.domain.Roles;
import com.eunhop.tmdbmovieapp.domain.User;
import com.eunhop.tmdbmovieapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserSecurityService implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    Optional<User> user = this.userRepository.findByEmail(email);
    if (user.isEmpty()) {
      throw new UsernameNotFoundException("사용자를 찾을수 없습니다.");
    }
    User getUser = user.get();
    User authUser = new User(getUser.getUsername(), getUser.getPassword(), getUser.getNickname(), getUser.getRole(), getUser.isEnabled());
    log.info("authUser: {}", authUser);
    return authUser;
  }
}
