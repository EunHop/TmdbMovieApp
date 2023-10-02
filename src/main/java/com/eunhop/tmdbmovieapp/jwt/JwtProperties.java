package com.eunhop.tmdbmovieapp.jwt;

import lombok.Getter;

public enum JwtProperties {
  // cookie.setMaxage()는 초 단위 이고 jwts.builder().setExpiration()은 밀리초 단위 이다
  // 밀리초 = 1초 * 1000, 초단위를 기준으로 해서 Expriation에는 * 1000 해줌
  ACCESS_EXPIRATION_TIME("15"), // 15초
  REFRESH_EXPIRATION_TIME("600"), // 10분
  ACCESS_TOKEN_COOKIE_NAME("JWT-ACCESS-TOKEN"),
  REFRESH_TOKEN_COOKIE_NAME("JWT-REFRESH-TOKEN");

  @Getter
  private final String description;

  JwtProperties(String description) {
    this.description = description;
  }
}
