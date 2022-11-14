<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%-- 문자열 관련 메서드를 제공하는 JSTL -> EL형식을 사용 ${fn: } --%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>myPage</title>
    <link rel="stylesheet" href="/resources/css/main-style.css">
    <link rel="stylesheet" href="/resources/css/myPage-style.css">

    
    <script src="https://kit.fontawesome.com/f7459b8054.js" crossorigin="anonymous"></script>

</head>
<body>
    <main>
        <jsp:include page="/WEB-INF/views/common/header.jsp" /> 

        <section class="myPage-content">
            
            <!-- <section class="left-side"> 
                사이드메뉴
                <ul class="list-group">
                    <li><a href="/member/myPage/profile">프로필</a></li>
                    <li><a href="/member/myPage/info">내 정보</a></li>
                    <li><a href="/member/myPage/changePw">비밀번호 변경</a></li>
                    <li><a href="/member/myPage/delete">회원 탈퇴</a></li>
                </ul>
            </section> -->
            <jsp:include page="/WEB-INF/views/member/sideMenu.jsp"/>
            
            <section class="myPage-main">
                <h1 class="myPage-title">프로필</h1>
                <span class="myPage-subject">프로필 이미지를 변경할 수 있습니다.</span>

                <!-- 
                    form태그의 enctype속성 
                    - 서버로 제출되는 데이터의 인코딩 방식을 지정
                    1) application/x-www-form-urlencoded (기본값)
                        - 제출되는 데이터를 서버로 전송하기 전에
                        모두 다 문자열 형식으로 인코딩
                        - 문자형식(String)만 제출 가능한 형식(X 파일형식은 불가 X)

                    2) multipart/form-data (무조건 POST방식)
                        - 모든 문자를 인코딩 하지 않고 데이터 전송
                        (제출되는 모든 데이터 인코딩 X)
                        - 파일은 파일, 문자열은 문자열로 전달
                        단, 서버에서 파일/문자열에 대한 별도 처리가 필요

                        - Apache Commons FileUpload  라이브러리 사용(pom.xml에 추가)하고,
                            root-context.xml에 "multipartResolver" bean등록
                    
                -->

                <!-- /member/myPage/updateProfile 를 상대경로로 요청 -->
                <form action="updateProfile" method="post" name="myPage-frm" enctype="multipart/form-data">
                    <div class="profile-image-area">
                        <img id="profile-image" src="/resources/images/user.png" alt="profile-image">
                    </div>
                    <span id="delete-image">&times;</span>

                    <div class="profile-btn-area">
                        <label for="image-input">이미지 선택</label>
                        <input type="file" name="profileImage" id="image-input" accept="image/*">
                                                                            <!-- accept 속성 : 업로드 가능한 파일의 타입을 제한하는 속성 -->
                        <button>변경하기</button>
                    </div>

                    <div class="myPage-row">
                        <label>이메일</label>
                        <span>user04@kh.or.kr</span>
                    </div>

                    <div class="myPage-row">
                        <label>가입일</label>
                        <span>2022년 10월 27일 10시 39분 12초</span>
                    </div>
                    
                </form>

            </section>
        </section>


    </main>

    <jsp:include page="/WEB-INF/views/common/footer.jsp" /> 
    <script src="/resources/js/member/myPage.js"></script>
</body>
</html>