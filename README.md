# TmdbMovieApp
Tmdb API를 활용한 영화 검색 사이트
엔티티 만들고 시큐리티 jwt 토큰 구현했다 회원가입과 로그인까지 확인했다
jwt AccessToken 과 RefreshToken으로 토큰 2개를 쿠키에 담아서 https secure했다
AccessToken, RefreshToken 둘다 있을때 데이터베이스와 비교하고 맞으면 다음 필터로 인가했다
AccessToken이 없으면 데이터베이스에서 accessToken을 가져와 만료되었는지 확인하고 만료됐다면 재발급을, 
만료안되었으면 RefreshToken이 탈취된 거로 간주해 데이터베이스 토큰을 지우고 쿠키를 만료시켰다. 
accessToken만 있는경우는 RefreshToken이 없어서 만료시키고 재로그인 시켰다.
