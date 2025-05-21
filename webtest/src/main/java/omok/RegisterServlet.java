package omok;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import db.OracleDBUtil;

/**
 * Servlet implementation class RegisterServlet
 */
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        String userId = request.getParameter("user_id");
        String password = request.getParameter("password");
        String nickname = request.getParameter("nickname");

        String sql = "INSERT INTO omok_user (user_id, password, nickname) VALUES (?, ?, ?)";

        try (Connection conn = OracleDBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, userId);
            pstmt.setString(2, password); // 보안상 암호화 추천
            pstmt.setString(3, nickname);
            pstmt.executeUpdate();

            response.sendRedirect("login.jsp");
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("회원가입 실패: " + e.getMessage());
        }
    }
}
