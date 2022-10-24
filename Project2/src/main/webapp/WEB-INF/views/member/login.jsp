<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>로그인 화면</title>
    
    <link rel="stylesheet" href="/resources/css/login-style.css">

    <!-- fontawesome사이트의 아이콘을 이용 -->
    <script src="https://kit.fontawesome.com/f7459b8054.js" crossorigin="anonymous"></script>
</head>
<body>
    <main>
        <section class="logo-area">
            <a href="/">
                <img src="/resources/images/logo.jpg" alt="logo">
            </a>
        </section>

        <form action="/member/login" method="POST">
            <section class="input-box">
                <!-- required 속성 : form태그 제출 시 해당 input태그에 값이 존재하는 검사 -->
                <input type="text" name="inputEmail" placeholder="Email" required value="${cookie.saveId.value}">
            </section>

            <section class="input-box">
                <!-- required 속성 : form태그 제출 시 해당 input태그에 값이 존재하는 검사 -->
                <input type="password" name="inputPw" placeholder="Password" required>
            </section>

            <button class="login-btn">Login</button>
        <%-- 쿠키에 saveId가 있는 경우에 변수 생성 --%>
        <c:if test="${!empty cookie.saveId.value}">
            <c:set var="temp" value="checked" />
        </c:if>

        <div class="saveId-area">
            <input type="checkbox" name="saveId" id="saveId" ${temp}>
            <label for="saveId">
                <i class="fas fa-check"></i> 아이디 저장
            </label>
        </div>

        <p class="text-area">
            <a href="/member/signUp">회원가입</a>
            |
            <a href="#">ID/PW찾기</a>
        </p>

    </main>

    <c:if test="${!empty sessionScope.message}">
        <script> 
            alert("${sessionScope.message}");
        </script> 
        <%-- message 1회 출력 후 session scope에서 삭제 --%>
        <c:remove var="message" scope="session" />
    </c:if>
</body>
</html>