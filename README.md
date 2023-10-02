# TmdbMovieApp
## Tmdb API를 활용한 영화 검색 사이트
OAath2 구현 완료했다.

일반로그인과 똑같이 jwt토큰을 발급받는다.

yaml 파일에 키값 떄문에 일단 gitignore 시켰다.

소셜로그인은 카카오, 네이버, 구글이 가능하고 oauth2 테이블을 만들어서 user 테이블과 다대일로 매칭시켰다.

dto.security에 PrincipalUser에 UserDetails와 OidcUser를 implement해

CustomOAuth2UserService, GoogleOAuth2UserService에 리턴타입으로 지정했다.

application-API-KEY.properties를 만들어서 개인정보를 숨기고 application.yaml을 배포할 수 있게 되었다.

개발중이라 /actuator이나 halexplorer의 /api 를 사용하기 위해 SecurityConfig에 // authorization 부분에 .anyRequest().permitAll() 로 바꿨다.

domain 패키지에 entity들에 @Data 부분을 @Getter @Setter로 바꿨다.
