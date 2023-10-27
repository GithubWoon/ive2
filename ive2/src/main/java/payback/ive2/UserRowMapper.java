package payback.ive2;

import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setId(rs.getString("ID"));
        user.setPassword(rs.getString("PW"));
        // 여기서 User는 당신이 정의한 도메인 클래스입니다.
        // 추가적인 필드가 있다면, 여기서 설정해주시면 됩니다.
        return user;
    }
}
