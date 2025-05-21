<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>회원가입 - 천하제일 오목대회</title>
    <link rel="stylesheet" href="style.css">
    <style>
        .form-container {
            background-color: rgba(255, 255, 255, 0.85);
            padding: 40px;
            border-radius: 12px;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.3);
            max-width: 400px;
            margin: 100px auto;
            text-align: center;
            font-family: 'Nanum Myeongjo', serif;
        }

        .form-container h2 {
            font-size: 32px;
            margin-bottom: 20px;
            color: #8B4513;
        }

        .form-container input {
            width: 100%;
            padding: 12px;
            margin-bottom: 15px;
            border: 1px solid #ccc;
            border-radius: 5px;
            font-family: 'Nanum Myeongjo', serif;
        }

        .form-container button {
            width: 100%;
            padding: 12px;
            background: linear-gradient(to bottom, #DCB35C, #b89346);
            color: #333;
            border: none;
            border-radius: 5px;
            font-weight: bold;
            font-size: 16px;
            cursor: pointer;
            transition: background 0.3s ease;
        }

        .form-container button:hover {
            background: linear-gradient(to bottom, #e6c37a, #DCB35C);
        }

        .form-container a {
            display: block;
            margin-top: 10px;
            color: #8B4513;
            text-decoration: none;
            font-weight: bold;
        }

        .form-container a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
<div class="form-container">
    <h2>회원가입</h2>
    <form action="register" method="post">
        <input type="text" name="user_id" placeholder="아이디" required>
        <input type="password" name="password" placeholder="비밀번호" required>
        <input type="text" name="nickname" placeholder="닉네임" required>
        <button type="submit">회원가입</button>
    </form>
    <a href="login.jsp">👈 로그인 하러가기</a>
</div>
</body>
</html>
