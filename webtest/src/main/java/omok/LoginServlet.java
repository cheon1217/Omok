package omok;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import db.OracleDBUtil;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        String userId = request.getParameter("user_id");
        String password = request.getParameter("password");

        String sql = "SELECT nickname FROM omok_user WHERE user_id = ? AND password = ?";

        try (Connection conn = OracleDBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, userId);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String nickname = rs.getString("nickname");
                HttpSession session = request.getSession();
                session.setAttribute("user_id", userId);
                session.setAttribute("nickname", nickname);

                response.sendRedirect("index.jsp");
            } else {
                response.getWriter().write("로그인 실패: ID 또는 비밀번호 확인");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("DB 오류: " + e.getMessage());
        }
    }
}
