새로 적용시킨 기술
1. 서비스
- ExceptionHandler를 사용하여 런타임에러를 상속받아 따로 관리하여 에러를 IllegalStateException으로 통일하는것이 아닌 종류별로 분리를하여 관리하였다
- #을 사용하면 태그인 것을 이용하여 PostService.searchAll 부분에 간단하게 동적검색기능을 넣어보았다.

2. 리포지토리
- 친구추가 요청, 거절, 수락을 관리할때 DB에 값을 두개 저장하도록하여 관리하였다. 요청시 한명은 REQUEST, 한명은 WAITING 거절 시 DB에서 삭제, 수락시 FRIEND, FRIEND로 만들어 관리하였다 친구 관계 하나 당 DB 두칸을 차지하므로 더 나은 방법을 모색해봐야겠다.
- 태그 기반 앱이기 때문에 태그를 회원, 게시글에 붙힐 수 있었는데 태그 관리자를 따로 만들어서 게시글에 달릴 시 회원은 null, 회원에 달 경우 게시글은 null로 관리하였다. null 값이 DB에 들어가 있어도 되는지 모르겠다.
- PostRepository에서 left join fetch를 사용했다. 단일 게시글과 해당 게시글의 댓글 모두 조회하는 쿼리였는데, 댓글이 없어도 가져와야 하므로 left join을 사용하였다.

3. 컨트롤러 - 컨트롤러에서 정말 많은것들을 배웠다.
- 회원가입 시 비밀번호를 패스워드 인코더로 DB에 패스워드를 인코딩 된 값을 저장하였다.
- try-catch문을 사용하여 서비스 부분에서 나온 에러를 잡아내어 사용자에게 e.getMessage로 에러를 보여주는 것이 정말 편리했다.
- 로그인 할 때 HttpSession session = request.getSession();을 사용하여 세션이 있으면 불러오고, 없으면 새로 생성하는 것을 배웠다. (getSession(false)를 넣어주면 새로 생성되는 것을 막는다.) 보안 요소를 더 배우면 좋을 것 같다.
- @SessionAttribute를 사용하여 세션안에 있는 내용을 바로 꺼낼 수 있었다.
- DTO에 생성자를 엔티티를 넣도록 설계하여 Entity에서 Dto의 변환이 용이하도록 했다.
- 엔티티가 아닌 DTO에 Validation을 사용하여 값이 컨트롤러 부분에서 들어올 때 한번 걸러질 수 있도록 하였다. 또한 @NotNull, @NotEmpty, @NotBlank의 차이를 알았다.
  ㄴ @NotNull = Null만 안됨, @NotEmpty = Null, "" 안됨, @NotBlank = Null, "", " " 안됨



앞으로 할 것
타임리프 배우기, mvc 강의 조금 더 듣고
적용시켜서 간단한 사이트 만들어보기

추후에는 Spring Security 공부해서 적용시키고싶다.

24.02.18 @ModelAttribute 이름 지정때문에 너무 고생했다. 이름을 지정해주지않으면 클래스명에서 맨 앞글자 소문자로 바꿔서 그대로 넘어가는 것을 깨달았다. 까먹지말자

24.02.20 게시글 번호를 PK값 그대로 ID를 가져와서 하는게 아닌, 새로운 count라는 필드를 만들어서 채번하였다. 없으면 1로 생성 있으면 기존거의 +1
'삭제를 구현할 때 채번을 어떤식으로 가야할까'가 문제다 기존의 게시글을 삭제할 때 비는 번호는 어떻게 할 것인가
-> chatgpt한테 물어본 결과 = 게시글 삭제를 논리적 삭제로 기존 게시글을 "삭제된 게시물 입니다" 로 나오도록 하여 게시글을 물리적으로 아예 없애버리는 것이 아닌 치환해버리는 식으로 하여 게시글의 번호에 아예 영향이 가지 않도록 하는 것이다.
-> 그래서 나중에 게시글을 잘못 삭제했을 경우 DB에는 작성한 게시글이 그대로 남아있으므로 복구해주는 방법 사용
이 방법 괜찮은것같다.

24.02.21 단일 게시글에서 댓글까지 모두 보이도록, 댓글도 작성할 수 있도록 만들어놓았다.
당장은 논리적 삭제가 아닌 cascade와 orphanRemoval을 사용하여 게시글에 딸린 댓글까지 모두 지워지도록 물리적 삭제로 구현하였다.
또한 게시글 수정에서 태그에 대한 로직의 조건문에 문제가 생겨서 수정하여 오류를 고쳤다.
TODO 친구목록 만들어서 친구 관리하기

24.02.23 ArgumentResolver @Login 생성, Interceptor 생성
![image](https://github.com/thstkddnr20/sharing-hobbies/assets/79399385/df99ac88-0e90-47eb-a41d-323d5f1d7420)
원리
1. 핸들러 어댑터에서 동작하는 ArgumentResolver에서(여기선 LoginArgumentResolver) 세션에 Member정보가 있는지 '찾음' 있으면 그대로 반환, 없으면 null 반환
2. 그 후 컨트롤러(핸들러)를 호출하기 직전에 호출되는 Interceptor에서 whitelist(excludePathPatterns)를 제외한 링크 접속 시 preHandle에서 세션을 '검증' 검증 실패 시 로그인 화면으로 리다이렉트하며 false 반환, 성공시 true 반환하여 컨트롤러 호출

말그대로 ArgumentResolver는 그저 세션에 Member가 있는지 Annotation을 이용하여 찾아오는 역할, Interceptor는 Member를 검증하는 역할을 한다.

24.02.24 04:21 인터셉터에서 쿼리파라미터로 보낸 값이 컨트롤러에 전달이 안되는 문제가 있었다.
오픈카톡방의 귀인을 만나 해결하였다.
기존 코드 <form th:action="@{/login}" th:object="${memberRequest}" method="post"> -> 변경 후 <form th:action th:object="${memberRequest}" method="post">
변경전, 변경후 코드를 올렸더니 챗gpt의 대답 : 이 경우, th:action을 비워두었으므로, 현재 페이지의 URL을 대상으로 폼이 제출됩니다.
원래는 절대경로로 딱 /login으로 들어오는 것만 폼으로 제출하였고, th:action을 비워놓아 쿼리파라미터가 붙은 url 전부를 폼으로 제출한 것이다. ex) /login?redirectURL=/posts/new

24.02.24 친구기능 추가완료하였다
친구목록, 친구추가, 친구요청거절, 친구요청수락 기능을 넣어 친구 기능을 만들었다.
다음으로 할 것은 한번 점검할 시간을 가져야 할 것같다. 성능확인을 위하여 DB쿼리가 몇방 나가는지도 확인하여 적어놔야 할 것 같다.




