<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Error</title>

    <style>
        body{
            display: flex;
            justify-content: center;
            text-align: center;
            height: 100vh;
        }

        .error-container {
            width : 800px;
            height : 300px;
            text-align: center;
        }

        .error-container > h1 {
            margin-bottom: 50px;
        }

        .error-container-title {
            text-align: left;
            font-weight: bold;    
        }

        .btn-area{
            text-align: center;
        }

    
    </style>
</head>
<body>
    <div class="error-container">
        <h1>${errorMessage}</h1>

        <span class="error-content-title">발생한 예외 : ${e}</span>  

        <p>자세한 문제의 원인은 이클립스 콘솔을 확인해주세요.</p>

        <div class="btn-area">
            <a href="/">메인 페이지</a>

            <button type="button" onclick="history.back()">뒤로가기</button>
        </div>
    </div>
    
</body>
</html>