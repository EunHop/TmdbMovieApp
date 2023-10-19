# TmdbMovieApp
## Tmdb API를 활용한 영화 검색 사이트

MovieService에서 외부 API 받아오는 코드를 HttpClient에서 WebClient로 바꿨다. 

---
API 요청하는 코드가 계속 반복되서 제네릭 메소드로 만들어서 반복되는 코드를 줄였다. 

---
WebFlux를 활용해 비동기로 처리하고 싶었지만 내부 데이터를 꺼내와 타임리프에서 

데이터를 받아오려면 block처리 할 수 밖에 없었고 대신 MovieController에서 

CompletableFuture를 활용해 외부 API 요청 메소드 마다 비동기로 처리되도록 만들었다.

---
상세정보 페이지에서 주요 제작진, 주요 출연진 부분을 구현했다. 주요 제작진에 사진이 없거나 

아예 데이터가 없으면 주요 제작진 문구가 안보이도록 처리했다.(TV 프로그램에 그런 부분이 있었다)

---
Movie와TV프로그램을 합쳐서 한단어로 표현할게 뭐가 있을까 하다가 Video로 하기로 결정했다. 

그래서 기존 Movie였던 이름을 Video로 바꿨다. 

---
VideoController에서 @AuthenticationPrincipal을 쓰려는데 

PrincipalUser가 자꾸 null로 넘어왔다. 

그래서 UsernamePasswordAuthenticationToken을 만드는 곳에

principal 매개변수를 고쳐야한다는걸 배웠고 

principal 매개변수로 UserDetails가 들어가기 위해서 

CustomUserDetailsService를 만들어서 loadUserByUsername 메소드를 구현하고 

이 메소드의 리턴값이 UserDetails 이므로 바로 넣어줬다.

---
회원가입, 로그인 예외 처리 했다.

css 만지고 홈페이지와 search페이지를 홈페이지 하나로 쓰던거를 나눴다.

---
소셜 로그인이 이메일이 같은거만 연관관계 매핑하도록 바꿨다. 

원래 EmailorName이였는데 동명이인 있을까봐 바꿨다.

---
details에 찜 목록과 관심 목록 등록 구현, my_wishlist에 찜 목록을 구현했다. 

my_wishlist의 모바일 화면은 구현 진행중