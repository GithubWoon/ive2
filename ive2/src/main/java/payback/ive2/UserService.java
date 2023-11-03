package payback.ive2;

import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.List;
import java.sql.PreparedStatement;
import javax.servlet.http.HttpSession;

@Service
public class UserService {
    private final JdbcTemplate jdbcTemplate;

    public UserService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public String login(String id, String password, HttpSession session) {

        // 사용자 조회
        List<User> users = jdbcTemplate.query(
                con -> {
                    PreparedStatement ps = con.prepareStatement("SELECT * FROM USERS WHERE ID = ? AND PW = ?");
                    ps.setString(1, id);
                    ps.setString(2, password);
                    return ps;
                },
                new UserRowMapper());

        if (!users.isEmpty()) {
            session.setAttribute("userId", users.get(0).getId());
            session.setAttribute("userName", users.get(0).getName());
            return "/userMenu";
        }

        // 매니저 조회
        List<Manager> managers = jdbcTemplate.query(
                con -> {
                    PreparedStatement ps = con.prepareStatement("SELECT * FROM MANAGER WHERE ID = ? AND PW = ?");
                    ps.setString(1, id);
                    ps.setString(2, password);
                    return ps;
                },
                new ManagerRowMapper());

        if (!managers.isEmpty()) {
            session.setAttribute("managerId", managers.get(0).getId()); // 세션에 관리자 ID 저장
            session.setAttribute("mangerName", managers.get(0).getName()); // 세션에 관리자 이름 저장
            return "/managerMenu";
        }

        return "로그인 실패";
    }

    public String signup(String id, String password, String name) {
        try {
            // USERS 테이블에서 ID 중복 체크
            List<User> users = jdbcTemplate.query(
                    con -> {
                        PreparedStatement ps = con.prepareStatement("SELECT * FROM USERS WHERE ID = ?");
                        ps.setString(1, id);
                        return ps;
                    },
                    new UserRowMapper());

            if (!users.isEmpty()) {
                return "이미 존재하는 ID입니다. 다른 ID를 생성하세요.";
            }

            // MANAGER 테이블에서 ID 중복 체크
            List<Manager> managers = jdbcTemplate.query(
                    con -> {
                        PreparedStatement ps = con.prepareStatement("SELECT * FROM MANAGER WHERE ID = ?");
                        ps.setString(1, id);
                        return ps;
                    },
                    new ManagerRowMapper());

            if (!managers.isEmpty()) {
                return "이미 존재하는 ID입니다. 다른 ID를 생성하세요.";
            }

            jdbcTemplate.update("INSERT INTO USERS (ID, PW, NAME) VALUES (?, ?, ?)", id, password, name);
            return "회원가입 성공";
        } catch (Exception e) {
            return "회원가입 과정에서 오류가 발생했습니다.";
        }
    }

    // UserRowMapper and ManagerRowMapper are similar to before but map to the
    // respective classes.
}
