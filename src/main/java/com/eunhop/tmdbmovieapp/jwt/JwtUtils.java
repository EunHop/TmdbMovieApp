package com.eunhop.tmdbmovieapp.jwt;

import com.eunhop.tmdbmovieapp.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import org.springframework.data.util.Pair;

import java.security.Key;
import java.time.LocalDate;
import java.util.Date;

public class JwtUtils {
    /**
     * 토큰에서 userEmail 찾기
     *
     * @param token 토큰
     * @return userEmail
     */
    public static String getUserEmail(String token) {
        // jwtToken에서 userEmail 을 찾습니다.
        return Jwts.parserBuilder()
                .setSigningKeyResolver(SigningKeyResolver.instance)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject(); // username
    }

    /**
     * access 토큰에서 만료날이 지났는지 확인하기 (안 지나면 true)
     *
     * @param token 토큰
     * @return boolean
     */
    public static boolean accessTokenNotExpired(String token) {
         Date expiration = Jwts.parserBuilder()
            .setSigningKeyResolver(SigningKeyResolver.instance)
            .build()
            .parseClaimsJws(token)
            .getBody()
            .getExpiration();
         Date now = new Date();
      return now.before(expiration);
    }

    /**
     * user로 Access 토큰 생성
     * HEADER : alg, kid
     * PAYLOAD : sub, iat, exp
     * SIGNATURE : JwtKey.getRandomKey로 구한 Secret Key로 HS512 해시
     *
     * @param user 유저
     * @return jwt token
     */
    public static String createAccessToken(User user) {
        Claims claims = Jwts.claims().setSubject(user.getUsername()); // subject
        Date now = new Date(); // 현재 시간
        Pair<String, Key> key = JwtKey.getRandomKey();
        // JWT Token 생성
        return Jwts.builder()
                .setClaims(claims) // 정보 저장
                .setIssuedAt(now) // 토큰 발행 시간 정보
                .setExpiration(new Date(now.getTime() + Integer.parseInt(JwtProperties.ACCESS_EXPIRATION_TIME.getDescription()))) // 토큰 만료 시간 설정
                .setHeaderParam(JwsHeader.KEY_ID, key.getFirst()) // kid
                .signWith(key.getSecond()) // signature
                .compact();
    }
    /**
     * user로 Refresh 토큰 생성
     * HEADER : alg, kid
     * PAYLOAD : sub, iat, exp
     * SIGNATURE : JwtKey.getRandomKey로 구한 Secret Key로 HS512 해시
     *
     * @param user 유저
     * @return jwt token
     */
    public static String createRefreshToken(User user) {
        Claims claims = Jwts.claims().setSubject(user.getUsername()); // subject
        Date now = new Date(); // 현재 시간
        Pair<String, Key> key = JwtKey.getRandomKey();
        // JWT Token 생성
        return Jwts.builder()
            .setClaims(claims) // 정보 저장
            .setIssuedAt(now) // 토큰 발행 시간 정보
            .setExpiration(new Date(now.getTime() + Integer.parseInt(JwtProperties.REFRESH_EXPIRATION_TIME.getDescription()))) // 토큰 만료 시간 설정
            .setHeaderParam(JwsHeader.KEY_ID, key.getFirst()) // kid
            .signWith(key.getSecond()) // signature
            .compact();
    }
}
