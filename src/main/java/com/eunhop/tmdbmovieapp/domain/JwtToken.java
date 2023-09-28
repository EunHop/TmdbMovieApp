package com.eunhop.tmdbmovieapp.domain;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "jwt_token")
@Entity
public class JwtToken {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String accessToken;

  private String refreshToken;

  @OneToOne
  @JoinColumn(name = "user_id")
  private User user;

  public JwtToken(String accessToken, String refreshToken) {
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;;
  }
}
