package com.eunhop.tmdbmovieapp.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user")
@Entity
public class User extends BaseEntity implements UserDetails {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  // @Column(nullable = false)은  엔티티의 필드 값이 null로 채워진 상태에서도 정상적으로 수행되다가 데이터베이스 쪽으로 SQL 쿼리가 도착한 순간에 예외가 발생하고
  // @NotNull은 엔티티의 필드 값이 null로 채워지는 순간 예외가 발생하므로 @NotNull이 더 전단계에서 예외 발생시키므로 훨씬 더 안전하다. 2023/09/28
  private String email;

  @NotNull
  private String password;

  @NotNull
  private String nickname;

  @Enumerated(EnumType.STRING) @NotNull
  private Roles role;

  private boolean enabled;


  @OneToMany
  @JoinColumn(name = "user_id")
  @ToString.Exclude
  private List<UserAndMovie> userAndMovies = new ArrayList<>();

  public User(String email, String password, String nickname, Roles role, boolean enabled) {
    this.email = email;
    this.password = password;
    this.nickname = nickname;
    this.role = role;
    this.enabled = enabled;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    Collection<GrantedAuthority> authorities = new ArrayList<>();
      authorities.add(new SimpleGrantedAuthority(role.getValue()));
    return authorities;
  }


  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return enabled;
  }

  @Override
  public boolean isAccountNonLocked() {
    return enabled;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return enabled;
  }

  @Override
  public boolean isEnabled() {
    return enabled;
  }
}
