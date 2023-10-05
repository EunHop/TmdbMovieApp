# TmdbMovieApp
## Tmdb API를 활용한 영화 검색 사이트
반응형 리팩토리 @media (max-width: 576px)를 통해 사이즈 조절했다.

홈 화면 구성, footer 구성, tmdbApi를 사용해서 인기 영화를 가져왔다.

api에서 데이터 가져올 때 어떻게 해야 원하는 데이터를 가져올 수 있을까 고민했다.

TMDB에 친절하게 가이드가 나와있어서 String 형식으로 넘어오니까 다시 Json형식으로 바꿔야 했고 
results [ {key: value, key: value, ...}, {key: value, key: value, ...}, ... ] 식이라서 results까지 JsonArray로 만들어서 

ex) String poster_path = list.get(i).getAsJsonObject().get("poster_path").getAsString();

for문으로 list.size() 만큼 데이터를 받아서 객체리스트로 만들어서 리턴값 지정하고 MovieController에서 model.addAttribute로 index 뷰단에 데이터를 넘길 수 있었다. 

Json 형식으로 바꿀때 new JsonParser()가 deprecated되서 어떻게 대체할 수 있을까 찾아보다가 해외사이트에서 찾아서 해결했다.

a태그 안에 이미지를 넣어야 하는데 헤매다가 th:style을 통해서 값을 넣을 수 있다는걸 알고 드디어 화면을 구성했다.

Json 형식으로 바꿀때 new JsonParser()가 deprecated되서 어떻게 대체할 수 있을까 찾아보다가 해외사이트에서 찾아서 해결했다.

JWT 토큰 시간 늘려서 작동하자 Long.parselong? 에서 오류가 난다. parse를 안쓰도록 고쳤다. 

F12(개발자도구) 쿠키 만료시간이 정확히 9시간 차이 난다. 찾아보니 쿠키는 GMT 시각으로 표시되며 당연히 9시간 차이난다.
