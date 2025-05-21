<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String userId = (String) session.getAttribute("user_id");
    if (userId == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    String prefilled = request.getParameter("room_id");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>첫장의 오목대회 - 입장</title>
    <link rel="stylesheet" href="style.css">
    <style>
        .join-container {
            background-color: rgba(255, 255, 255, 0.85);
            padding: 40px;
            border-radius: 12px;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.3);
            max-width: 500px;
            margin: 120px auto;
            text-align: center;
            font-family: 'Nanum Myeongjo', serif;
            position: relative;
        }
        .logout-link {
            position: absolute;
            top: -60px;
            right: 0;
            font-weight: bold;
        }
        .join-container h2 {
            font-size: 30px;
            margin-bottom: 30px;
            color: #8B4513;
        }
        .join-container input[type="text"] {
            width: 80%;
            padding: 12px;
            margin-bottom: 20px;
            border: 1px solid #ccc;
            border-radius: 5px;
            font-size: 16px;
        }
        .join-container button {
            padding: 12px 24px;
            background-color: #8B4513;
            color: white;
            border: none;
            border-radius: 5px;
            font-size: 16px;
            cursor: pointer;
        }
        .join-container button:hover {
            background-color: #a05717;
        }
        .room-list-link {
            display: inline-block;
            margin-top: 20px;
            font-size: 16px;
            color: #8B4513;
            text-decoration: none;
        }
        .room-list-link:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
<div class="join-container">
    <div class="logout-link">
        👤 <%= userId %> | <a href="logout.jsp" style="text-decoration:none;color:#8B4513;">로그아웃</a>
    </div>

    <form method="post" action="room">
        <input type="hidden" name="action" value="create">
        <button type="submit">🔹 방 만들기</button>
    </form>

    <hr>

    <form method="post" action="room">
        <input type="hidden" name="action" value="join">
        <label>참가할 방 코드:</label><br>
        <input type="text" name="room_id" placeholder="6자리 코드" value="<%= prefilled != null ? prefilled : "" %>" required>
        <br><br>
        <button type="submit">🏃‍ 입장하기</button>
    </form>
    
    <a class="room-list-link" href="room.jsp">📝 방 목록 보기</a>
</div>
</body>
</html>
