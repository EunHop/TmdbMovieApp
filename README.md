TMDB API를 활용한 영화, TV프로그램 검색 사이트
---

![image](https://github.com/EunHop/TmdbMovieApp/assets/91999279/62638ab2-ab47-4a78-bfb9-d30f954d19a5)

배포 사이트 링크 = <https://port-0-tmdbmovieapp-1gksli2alps05xks.sel4.cloudtype.app>

• 프로젝트 내용 :

아무 권한 없이 영화 검색, 영화상세정보, 공지사항 확인 가능합니다.

유저 권한으로 로그인 할 경우 검색한 영화 찜하기와 찜한 영화들의 목록도 볼 수 있고

영화 상세정보에서 한줄평도 남길 수 있습니다.

어드민 권한으로 로그인 할 경우 유저 관리(한줄평 못쓰게 막기)와

공지사항 글쓰기 가능, 악의적인 한줄평 숨김처리를 할 수 있습니다.

---

• 기술 스택

백엔드

    Language : Java 17

    Framework : Spring Boot 3.1.4

    IDE : IntelliJ IDEA 2023.2 (Ultimate Edition)

    Build Tools : gradle-8.2.1

Library :

    Spring Data JPA, Spring Validation, DB = MariaDB(배포 사이트에 mysql이 없어서)

    Spring Security, Spring OAuth2 Client, jjwt-api, jjwt-impl, jjwt-jackson

    Spring Boot Web, Spring WebFlux,

    Spring devtools, Lombok

프론트엔드

    spring-boot-starter-thymeleaf, thymeleaf-extras-springsecurity6,

    javascript, ajax, jquery


---

Spring Security로 보안 구현하기

JWT

JWT(Json Web Token)는 토큰 기반 인증방식으로 인증에 필요한 정보들을 Token에 담아

암호화시켜 쿠키에 담아 사용합니다.

쿠키는 자바스크립트로 조회{CSS(Cross Site Scripting)공격}나 네트워크를 직접 감청해 탈취될 수

있습니다.

CSS공격과 네트워크 직접 감청을 막기 위해 쿠키를 생성할 때 HttpOnly와 Secure 옵션을 true로

지정해줬습니다.

사용되는 시점은 다른 페이지로 이동될 때입니다.(인가)

---

TMDB API 데이터 가져와서 활용하기

WebClient

스프링에서는 REST API를 호출해 간편하게 바로 객체에 담을 수 있는 RestTemplate가 있습니다.

근데 스프링 5부터 deprecated된다는 얘기가 있었고 현재는 maintenance mode라고 합니다.

maintenance 이기 때문에 RestTemplate로 사용해도 되지만 미래에 또 쓰게 될 때를 대비해

RestTemplate의 상위 호환 모듈인 WebClient로 써보기로 결정했습니다.

WebClient를 쓰기 위해서 Spring WebFlux를 추가해 줬습니다.

---

페이지 순서도

![image](https://github.com/EunHop/TmdbMovieApp/assets/91999279/09b0453a-9c17-4826-9165-f4e0c0efc04a)

---

개발하면서 기억에 남은 문제 - 뒤로가기

작품 상세정보 페이지에서 한줄평 또는 즐겨찾기 과정을 수행한 후에 새로고침이 되면서

뷰단에 DB데이터와 동기화가 됩니다.

하지만 제 생각과는 다르게 새로고침이 페이지 히스토리에 남으면서 뒤로가기 버튼을

누르면 같은 페이지가 반복되어버리는 문제가 발생했습니다.

해당 문제를 해결하기 위해서 자바스크립트에서 location.replace(location.href); 메소드를

사용하면 페이지 히스토리에 남기지 않는다는 것을 찾았습니다.

그래서 자바스크립트로 백엔드와 통신하기 위해서 ajax를 도입하기로 했습니다.

ajax는 자바스크립트에서 백엔드로 데이터를 넘겨줘서 백엔드에서 로직이 진행 후

성공적이면 location.replace(location.href); 메소드가 실행됩니다.

---

프로젝트의 특징 및 느낀점

제 프로젝트는 모바일 환경까지 구현하였습니다.

배포까지 되어있어 언제 어디서든 접근이 가능합니다.

제 첫 프로젝트로서 하나부터 열까지 모든걸 구현해보며 공부하는 시간을 가질 수 있었습니다.

개발하면서 문제에 직면했을 때 해결하는 능력을 많이 갖추게 되었습니다.

예를들어 위에서 다룬 페이지 히스토리가 계속 쌓여 뒤로가기가 안먹히는 문제에서

어떤 기술을 써야 해결할 수 있는지, 해당 기술을 쓰기위해 나에게 필요한 부분이 어디이고

어떤 부분을 활용해야하는지에 대한 효율적인 정보 검색을 통해서 이전보다 더 빠르게

정보를 찾을 수 있었습니다.

따라서 이전보다 더 문제 해결 능력을 키우게 되었습니다.

---

기대가치

사이트를 배포해놨기 때문에 언제 어디서나 모바일 환경이든 pc환경이든 인터넷만 된다면

사이트에 접속할 수 있습니다.

현재 어떤 작품이 인기가 있는지 알아보고 작품을 검색하는데에 사용할 것 같습니다.

관심목록도 활용하면서 개인 영화 평가 리스트?같은 느낌으로 활용해도 괜찮을 것 같습니다.

페이지에서 남들과 다른 차별점을 느끼기는 힘들지만 제 역량을 키워준 프로젝트로서

저한테는 큰 가치를 가질수 밖에 없는거 같습니다.

배포 사이트 링크 = <https://port-0-tmdbmovieapp-1gksli2alps05xks.sel4.cloudtype.app>


개선할 점

공지사항 검색기능

- 공지사항이 많아질수록 찾아보기 힘들다는 단점 개선

내 한줄평 보기

- 현재는 관심목록에서 해당 작품에 상세정보 페이지로 들어가야 확인 가능해서 좀 불편한

감이 있습니다

전체 테스트 코드 쭉 짜보기

- 실행하면서 다 확인해봤기에 치명적인 오류는 없지만 이것보다 테스트 코드를 짜는 법을

공부해 해당 내용에 관해 테스트를 진행만 하면 매번 다시 확인 할 필요가 없어져서 좋을 것

같습니다.

