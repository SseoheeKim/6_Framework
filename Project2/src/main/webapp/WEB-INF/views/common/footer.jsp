<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<footer>
    <p> 
        Copyright &copy; KH Information Educational Institute A-Class
        <!-- &copy;  저작권 마크 -->
    </p>
    <article>
        <a href="#">프로젝트 소개</a>
        <span>|</span>
        <a href="#">이용약관</a>
        <span>|</span>
        <a href="#">개인정보처리방침</a>
        <span>|</span>
        <a href="#">고객센터</a>
    </article>
</footer>

<%-- session scope 내에 message속성이 존재하는 경우
    alert(JS)기능을 이용해서 내용 출력 --%>

<%-- 왜 footer에 사용하는가?
    1) header/ footer는 고정적으로 계속 나타나는 JSP
    2) header는 너므 복잡행
    3) 프로그램이 진행되는 순서에 따라 중간에 알람이 뜨는 경우 다른 오류 발생 가능성 존재
      -> 따라서 마지막에 사용  --%>
<c:if test="${!empty sessionScope.message}">
    <script> 
        alert("${sessionScope.message}");
    </script> 
    <%-- message 1회 출력 후 session scope에서 삭제 --%>
    <c:remove var="message" scope="session" />
</c:if>