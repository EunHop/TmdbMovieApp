package com.eunhop.tmdbmovieapp.dto;

import com.eunhop.tmdbmovieapp.domain.JwtToken;
import com.eunhop.tmdbmovieapp.domain.Roles;
import com.eunhop.tmdbmovieapp.domain.UserAndMovie;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RegisterUserDto {
  private String email;
  private String password;
  private String nickname;
  private Roles authority;
  private boolean enabled;
}
