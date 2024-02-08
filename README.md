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