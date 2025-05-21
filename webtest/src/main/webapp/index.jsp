<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String userId = (String) session.getAttribute("user_id");
    String nickname = (String) session.getAttribute("nickname");
    String roomId = request.getParameter("room_id");
    if (userId == null || roomId == null || roomId.isEmpty()) {
        response.sendRedirect("join.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>천하제일 오목대회</title>
    <link rel="stylesheet" href="style.css">
    <script>
        const roomId = "<%= roomId %>";
    </script>
    <script src="script.js" defer></script>
</head>
<body>
<div class="mountain-bg">
    <div class="mountain"></div>
    <div class="mountain"></div>
    <div class="mountain"></div>
</div>
<div class="trees">
    <div class="tree"></div>
    <div class="tree"></div>
    <div class="tree"></div>
    <div class="tree"></div>
</div>

<div class="intro-overlay">
    <div class="intro-title">천하제일 오목대회</div>
    <div class="intro-text">반상의 승부, 천하무적의 명예를 향해!</div>
    <div class="waiting-message">🕐 참가자 기다리는 중...</div>
</div>

<div style="position: absolute; top: 10px; right: 20px; background-color: rgba(255,255,255,0.8); padding: 8px 16px; border-radius: 8px; font-weight: bold;">
    👤 <%= nickname %> 님 | <a href="logout.jsp" style="text-decoration: none; color: #8B4513;">로그아웃</a>
</div>

<div class="container">
    <h1 id="game-title">OMOK 전장 (방 코드: <%= roomId %>)</h1>
</div>

<div class="board-container">
    <div id="board"></div>
</div>

<div id="game-info">
    <div id="status-message">🕐 참가자 기다리는 중...</div>
    <div id="turn-indicator" class="black">흑돌 차례입니다</div>
    <button id="restart-btn">다시 시작</button>
</div>

<div class="win-overlay">
    <div class="win-content">
        <div class="win-message">승리!</div>
        <div class="martial-message">천하무적 승리의 순간!</div>
        <button id="close-win">닫기</button>
    </div>
</div>
</body>
</html>
