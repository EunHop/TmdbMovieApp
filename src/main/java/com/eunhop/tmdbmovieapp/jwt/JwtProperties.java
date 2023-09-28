package com.eunhop.tmdbmovieapp.jwt;

import lombok.Getter;

public enum JwtProperties {
  ACCESS_EXPIRATION_TIME("600000"),
  REFRESH_EXPIRATION_TIME("1209600033"),
  ACCESS_TOKEN_COOKIE_NAME("JWT-ACCESS-TOKEN"),
  REFRESH_TOKEN_COOKIE_NAME("JWT-REFRESH-TOKEN");

  @Getter
  private final String description;

  JwtProperties(String description) {
    this.description = description;
  }
}
