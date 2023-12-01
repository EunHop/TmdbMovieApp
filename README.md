# TmdbMovieApp
## Tmdb API를 활용한 영화 검색 사이트

Postmapping 리턴에 redirect 쓸때 이전 페이지와 같은 페이지로 이동시 a -> redirect b-> c 가 되버려서 

한번 post 보낼때마다 페이지 히스토리가 하나씩 더 쌓여서 뒤로가기 눌러도 제자리걸음 이었다. 

그래서 어떻게 리팩토리 해야할까 구글링 하다가 location.replace(url) 라는 자바스크립트 함수를 쓰면 

해당 함수로 페이지 이동해도 히스토리가 안 쌓인다는걸 찾았고 자바스크립트로 백엔드와 상호작용하려면 ajax를 써야했다.

ajax가 어떤식으로 돌아가는지 찾아보는데까지 하루에 반을 쓴거 같다;;

ajax는 

$.ajax({ url: 'url', method: 'method'(type과 동일한거 같다), data : $('form id값').serialize(), success: function(){location.replace(url)} }) 

기본 이런식으로 생겼고 

data를 넘겨줄 때 $('form id값').serialize() 라는 form태그 안에 name이 붙은 태그들을 하나로 묶어서 보낼 수 있었고 하나로 묶은걸 컨트롤러에서 객체로 받을 수 있었다. 

그리고 form 태그들이 자바스크립트를 거치지않고 submit되서 컨트롤러로 넘어가면 안되기 떄문에 

form 태그에 onsubmit="return false"를 달아 줬고(이걸 안달아주면 GET 메소드로 날아간다) 

button태그에 type=submit을 type=button으로 바꾸고 id값을 주거나 onclick을 활용해서 자바스크립트로 넘겨 ajax를 타게 구현했다. 

form 태그에 onsubmit="return false"를 줬기 때문에 input type=text에 active 된 상태로 엔터를 눌러도 submit이 안됐고 엔터키 눌러서 ajax post를 날리기 위해서 자바스크립트에 해당 코드 추가해줬다.

---

Exceeded limit on max bytes to buffer : 262144

해당 오류가 떴다. 

그래서 열심히 찾아보니 메모리 누수를 막기위해 기본 메모리 사이즈를 256KB로 설정 되어있다는 것을 찾았고 
이 사이즈를 키우기 위해서 해당 코드를 추가 해주었다.

---

PrincipalUser에 권한이 잘 매칭이 안되어 있었다.

---

공지사항 페이지를 완료했다. 

어드민 권한으로 수정, 삭제, 추가, 삭제된 공지사항 확인이 가능하고 유저나 anonymous권한으로는 공지사항 보는 것만 가능하다.