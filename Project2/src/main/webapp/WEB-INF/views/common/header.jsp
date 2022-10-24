<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

    <header>
        <section>
            <!-- 클릭 시 메인페이지로 이동하는 로고 -->
            <a href="/">
                <img src="/resources/images/logo.jpg" id="home-logo">
            </a>
        </section>
        
        <!-- 검색창 영역  -->
        <section>
            <article class="search-area">
                <form action="#">
                <!-- form : 내부 input태그의 값을 서버/페이지로 전달(제출) -->
                    <fieldset>
                        <input type="text" id="query" name="query" placeholder="검색어를 입력해주세요.">
                        <button type="submit" id="search-btn" class="fa-solid fa-magnifying-glass"></button>
                    </fieldset>
                </form>
            </article>
        </section>

        <section></section>

        <%-- header 오른쪽 상단 메뉴 --%>
        <div id="header-top-menu">
            <c:choose>
                <c:when test="${empty sessionScope.loginMember}">
                    <a href="/">메인 페이지</a>
                    |
                    <a href="/member/login">로그인</a>
                </c:when>
                
                <%-- 로그인인 경우 --%>
                <c:otherwise>
                    <label for="header-menu-toggle">
                        ${loginMember.memberNickname}
                        <i class="fa-solid fa-caret-down"></i>
                    </label>
                   
                    <input type="checkbox" id="header-menu-toggle">

                    <div id="header-menu">
                        <a href="/member/myPage/info">내정보</a>
                        <a href="/member/logout">로그아웃</a>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </header>

    <nav>
        <ul>
            <c:forEach var="boardType" items="${boardTypeMap}">
            <%--
                    EL을 이용한 Map 데이터를 다루는 방법
                    key   ==> ${var명.key}
                    value ==> ${var명.value}
                --%>
                <li><a href="/board/${boardType.key}/list">${boardType.value}</a>
                </li>
        
            </c:forEach>
        </ul>
    </nav>