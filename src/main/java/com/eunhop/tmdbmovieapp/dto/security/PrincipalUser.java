package com.eunhop.tmdbmovieapp.dto.security;

import com.eunhop.tmdbmovieapp.domain.Roles;
import com.eunhop.tmdbmovieapp.domain.User;
import lombok.Builder;
import lombok.Data;
import lombok.Generated;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Getter
@Builder
public class PrincipalUser implements UserDetails, OidcUser {

  // 유저
  private User user;

  // OAuth2
  private Map<String, Object> attributes;


  public PrincipalUser(User user){
    this.user = user;
  }

  public PrincipalUser(User user, Map<String, Object> attributes) {
    this.user = user;
    this.attributes = attributes;
  }

  // 해당 user의 권한을 return하는 함수
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    Collection<GrantedAuthority> collect = new ArrayList<>();
    collect.add(new SimpleGrantedAuthority(Roles.USER.getValue()));
    return collect;
  }

  @Override
  public String getPassword() {
    return user.getPassword();
  }

  @Override
  public String getName() {
    return user.getName();
  }

  @Override
  public String getUsername() {
      return user.getEmail();
  }

  @Override
  public boolean isAccountNonExpired() {
    return user.isEnabled();
  }

  @Override
  public boolean isAccountNonLocked() {
    return user.isEnabled();
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return user.isEnabled();
  }

  @Override
  public boolean isEnabled() {
    return user.isEnabled();
  }

  @Override
  public Map<String, Object> getClaims() {
    return null;
  }

  @Override
  public OidcUserInfo getUserInfo() {
    return null;
  }

  @Override
  public OidcIdToken getIdToken() {
    return null;
  }
}