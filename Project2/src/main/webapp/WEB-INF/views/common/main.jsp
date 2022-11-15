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
    <title>수업용 프로젝트</title>
    
    <link rel="stylesheet" href="/resources/css/main-style.css">
</head>

<!-- fontawesome사이트의 아이콘을 이용 -->
<script src="https://kit.fontawesome.com/f7459b8054.js" crossorigin="anonymous"></script>

<body>

    <main>
        <%-- 
            jsp action tag : include 
            - 해당 위치에 page 속성으로 지정된 jsp파일의 내용이 포함
            - jsp 파일의 경로는 /webapp 폴더 기준으로 작성
        --%>

        <%-- header.jsp 추가(포함) --%>
        <jsp:include page="/WEB-INF/views/common/header.jsp" />

        <section class="content">
            <section class="content1"> 
                <div>
                    <h3>이메일로 회원 정보 조회(AJAX)</h3>
                    이메일 :  <input type="text" id="inputEmail">
                    <button id="selectEmail">조회</button>
                    <%-- 일치하는 화면이 있을 때, 없을 때는 요소 추가하기로 js파일에 추가 --%>
                </div>

                <div id="content1-2">
                    <h3>10초마다 모든 회원 정보를 조회</h3>
                    <table>
                        <thead>
                            <tr>
                                <th>회원번호</th>
                                <th>이메일</th>
                                <th>탈퇴여부</th>
                            </tr>
                        </thead>
                        
                        <tbody id="tbody">
                            <%-- <tr>
                                <td></td>
                                <td></td>
                                <td></td>
                            </tr> --%>
                        </tbody>
                        
                        <tfoot>
                            <tr>
                                <th>회원수</th>
                                <th colspan="2" id="memberCount"></th> 
                            </tr>
                        </tfoot>
                    </table>
                </div>
            </section>

        
            <section class="content2"> 
                <%--  로그인 여부에 따라 출력화면 변경 --%>
                <c:choose>
                    <c:when test="${empty sessionScope.loginMember}">
                        
                                                    <%-- JS와 연동하기 위해 name 사용 --%>
                        <form action="/member/login" name="login-frm" method="POST" onsubmit="return loginValidate()">
                                    <%-- 절대 경로 --%>              <%-- 비번 노출 방지 --%>

                        <%-- 
                            form태그의 submit이벤트를 취소시키는 방법 1
                            
                            인라인 이벤트 모델의 결과로 false를 리턴하면 제출 이벤트 취소
                            - 함수를 호출해서 아이디 또는 비밀번호가 입력이 안될 경우 false를 반환
                        --%>
                
                            <%--  fieldset 아이디, 비밀번호, 로그인 버튼 --%>
                            <fieldset id="id-pw-area">
                                <section>
                                    <input type="text" name="memberEmail" autocomplete="off" placeholder="이메일" value="${cookie.saveId.value}">
                                                                                                                <%-- 쿠키 중 saveId에 저장된 값 --%>
                                    <input type="password" name="memberPw" autocomplete="off" placeholder="비밀번호" >
                                </section>
                
                                <section>
                                    <button type="submit">로그인</button>
                                </section>
                            </fieldset>

                            <%-- cookie에 saveId가 있는 경우 --%>
                            <c:if test="${!empty cookie.saveId.value}">
                                <%-- temp 변수 선언 --%>
                                <c:set var="temp" value="checked" />
                                <%-- page scope로 page어디서든 사용 가능
                                    if문에서 벗어나도 사용 가능 --%>
                            </c:if>
                            
                            <%-- 로그인하기 
                                label태그 내부에 input태그를 작성하면 자동 연결--%>
                            <label>
                                <input type="checkbox" id="saveId" name="saveId" ${temp}> 아이디 저장
                            </label>
                
                            <!-- 회원가입/ ID/PW찾기 -->
                            <article id="signUp-find-area">
                                <a href="/member/signUp">회원가입</a>
                                <span>|</span>
                                <a href="#">ID/PW찾기</a>
                            </article>
                        </form>
                    </c:when>

                    <%-- 로그인 인 경우 --%>
                    <c:otherwise>
                        <article class="login-area">
                            
                            <!-- 회원 프로필 이미지-->
                            <a href="/member/myPage/profile">
                                <c:if test="${empty loginMember.profileImage}">    
                                    <img id="member-profile" src="/resources/images/user.png">
                                </c:if> 

                                <c:if test="${not empty loginMember.profileImage}">    
                                    <img id="member-profile" src="${loginMember.profileImage}">
                                </c:if> 
                            </a>
                            
                            <%-- 회원정보와 로그아웃 --%>
                            <div class="my-info">
                                <div>
                                    <a href="/member/myPage/info" id="nickname">${loginMember.memberNickname}</a>
                                    <a href="/member/logout" id="logout-btn">로그아웃</a>
                                </div>
                                <p>${loginMember.memberEmail}</p>
                            </div>
                        </article>       
                    </c:otherwise>
                </c:choose>
            </section>
        </section>
    </main>  

    <%-- footer.jsp 포함 --%>
    <jsp:include page="/WEB-INF/views/common/footer.jsp" />

    <%-- main.js 위에 cdn방식의 jQuery 추가 --%>
    <script src="https://code.jquery.com/jquery-3.6.1.min.js" integrity="sha256-o88AwQnZB+VDvE9tvIXrMQaPlFFSUTR+nldQm1LuPXQ=" crossorigin="anonymous"></script>
    <script src="/resources/js/main.js"></script>
</body>
</html>