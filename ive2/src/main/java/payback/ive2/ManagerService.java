package payback.ive2;

import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.List;
import java.sql.PreparedStatement;

@Service
public class ManagerService {
    private final JdbcTemplate jdbcTemplate;

    public ManagerService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public String addManager(String id, String password, String name) {
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

            jdbcTemplate.update("INSERT INTO MANAGER (ID, PW, NAME) VALUES (?, ?, ?)", id, password, name);
            return "관리자 추가 성공";
        } catch (Exception e) {
            return "관리자 추가 과정에서 오류가 발생했습니다.";
        }
    }

}
