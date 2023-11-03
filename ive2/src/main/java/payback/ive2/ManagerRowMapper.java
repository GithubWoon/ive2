package payback.ive2;

import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ManagerRowMapper implements RowMapper<Manager> {
    @Override
    public Manager mapRow(ResultSet rs, int rowNum) throws SQLException {
        Manager manager = new Manager();
        manager.setId(rs.getString("ID"));
        manager.setPassword(rs.getString("PW"));
        manager.setName(rs.getString("NAME")); // 관리자 이름 설정
        // 여기서 Manager는 당신이 정의한 도메인 클래스입니다.
        // 추가적인 필드가 있다면, 여기서 설정해주시면 됩니다.
        return manager;
    }
}
