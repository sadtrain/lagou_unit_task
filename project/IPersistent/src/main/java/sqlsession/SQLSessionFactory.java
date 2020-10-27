package sqlsession;

import pojo.Configuration;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class SQLSessionFactory {
    public synchronized SQLSession getSQLSession(Configuration configuration) throws SQLException {
        SQLSession sqlSession = new SQLSession(configuration);
        return sqlSession;
    }
}
