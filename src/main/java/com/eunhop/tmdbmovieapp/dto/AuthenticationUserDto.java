package com.eunhop.tmdbmovieapp.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthenticationUserDto {
  private Long id;
  private String email;
  private String nickname;
}
