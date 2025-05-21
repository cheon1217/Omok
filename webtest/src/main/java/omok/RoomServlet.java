package omok;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import db.OracleDBUtil;

@WebServlet("/room")
public class RoomServlet extends HttpServlet {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        String userId = (String) session.getAttribute("user_id");

        if (userId == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String action = request.getParameter("action");

        if ("create".equals(action)) {
            createRoom(userId, response);
        } else if ("join".equals(action)) {
            String roomId = request.getParameter("room_id");
            joinRoom(userId, roomId, response);
        } else if ("delete".equals(action)) {
            String roomId = request.getParameter("room_id");
            deleteRoom(userId, roomId, response);
        }
    }

    private void createRoom(String userId, HttpServletResponse response) throws IOException {
        String sql = "INSERT INTO omok_room (room_id, host_id) VALUES (?, ?)";

        try (Connection conn = OracleDBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String roomCode = generateRoomCode(conn);
            pstmt.setString(1, roomCode);
            pstmt.setString(2, userId);
            pstmt.executeUpdate();

            response.sendRedirect("index.jsp?room_id=" + roomCode);
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("방 생성 실패: " + e.getMessage());
        }
    }

    private String generateRoomCode(Connection conn) throws SQLException {
        String code;
        do {
            code = String.format("%06d", (int)(Math.random() * 1000000));
            String checkSql = "SELECT COUNT(*) FROM omok_room WHERE room_id = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setString(1, code);
                ResultSet rs = checkStmt.executeQuery();
                rs.next();
                if (rs.getInt(1) == 0) break;
            }
        } while (true);
        return code;
    }

    private void joinRoom(String userId, String roomCode, HttpServletResponse response) throws IOException {
        String sql = "UPDATE omok_room SET guest_id = ?, status = 'PLAYING' WHERE room_id = ? AND guest_id IS NULL";

        try (Connection conn = OracleDBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, userId);
            pstmt.setString(2, roomCode);
            int result = pstmt.executeUpdate();

            if (result > 0) {
                response.sendRedirect("index.jsp?room_id=" + roomCode);
            } else {
                response.sendRedirect("join.jsp?error=입장 실패: 방이 가득 찼거나 존재하지 않습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("join.jsp?error=입장 오류: " + e.getMessage());
        }
    }

    private void deleteRoom(String userId, String roomId, HttpServletResponse response) throws IOException {
        String sql = "DELETE FROM omok_room WHERE room_id = ? AND host_id = ?";

        try (Connection conn = OracleDBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, roomId);
            pstmt.setString(2, userId);
            int result = pstmt.executeUpdate();

            if (result > 0) {
                response.sendRedirect("room.jsp");
            } else {
                response.getWriter().write("삭제 실패: 본인이 만든 방만 삭제할 수 있습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("삭제 중 오류 발생: " + e.getMessage());
        }
    }
}
