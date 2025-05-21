<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.sql.*, db.OracleDBUtil" %>
<%
    String userId = (String) session.getAttribute("user_id");
    if (userId == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>첫장의 오목 방 목록</title>
    <link rel="stylesheet" href="style.css">
    <style>
        .room-list {
            background-color: rgba(255, 255, 255, 0.85);
            padding: 40px;
            border-radius: 12px;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.3);
            max-width: 800px;
            margin: 100px auto;
            font-family: 'Nanum Myeongjo', serif;
            position: relative;
        }
        .logout-link {
            position: absolute;
            top: -60px;
            right: 0;
            font-weight: bold;
        }
        .room-list h2 {
            font-size: 36px;
            color: #8B4513;
            text-align: center;
            margin-bottom: 20px;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }
        th, td {
            padding: 12px;
            border: 1px solid #ccc;
            text-align: center;
        }
        th {
            background-color: #DCB35C;
            color: #333;
        }
        td form {
            margin: 0;
        }
        button {
            padding: 8px 16px;
            font-family: 'Nanum Myeongjo', serif;
            background-color: #8B4513;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }
        button:hover {
            background-color: #a05717;
        }
        .create-room {
            text-align: center;
            margin-top: 30px;
        }
    </style>
</head>
<body>
    <div class="room-list">
        <div class="logout-link">
            👤 <%= userId %> | <a href="logout.jsp" style="text-decoration:none;color:#8B4513;">로그아웃</a>
        </div>

        <h2>대기 중인 오목 방</h2>
        <form method="post" action="room" class="create-room">
            <input type="hidden" name="action" value="create">
            <button type="submit">🔹 새 방 만들기</button>
        </form>
        <table>
            <tr>
                <th>방 코드</th>
                <th>호스트</th>
                <th>상태</th>
                <th>입장</th>
                <th>삭제</th>
            </tr>
            <%
                try (Connection conn = OracleDBUtil.getConnection();
                     PreparedStatement pstmt = conn.prepareStatement("SELECT room_id, host_id, status FROM omok_room WHERE status = 'WAITING'");
                     ResultSet rs = pstmt.executeQuery()) {

                    while (rs.next()) {
                        String roomId = rs.getString("room_id");
                        String hostId = rs.getString("host_id");
                        String status = rs.getString("status");
            %>
            <tr>
                <td><%= roomId %></td>
                <td><%= hostId %></td>
                <td><%= status %></td>
                <td>
                    <form method="post" action="room">
                        <input type="hidden" name="action" value="join">
                        <input type="hidden" name="room_id" value="<%= roomId %>">
                        <button type="submit">참가</button>
                    </form>
                </td>
                <td>
                    <% if (userId.equals(hostId)) { %>
                    <form method="post" action="room">
                        <input type="hidden" name="action" value="delete">
                        <input type="hidden" name="room_id" value="<%= roomId %>">
                        <button type="submit">삭제</button>
                    </form>
                    <% } else { %>
                    -
                    <% } %>
                </td>
            </tr>
            <%
                    }
                } catch (Exception e) {
                    out.println("<tr><td colspan='5'>오류 발생: " + e.getMessage() + "</td></tr>");
                }
            %>
        </table>
    </div>
</body>
</html>
