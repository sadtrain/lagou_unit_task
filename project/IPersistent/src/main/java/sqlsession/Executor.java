package sqlsession;

import pojo.Configuration;
import pojo.MappedStatement;

import java.sql.SQLException;
import java.util.List;

/**
 * @Description :
 * @Author : ZGS
 * @Date: 2020-10-11 13:33
 */
public interface Executor {

    public <E> List<E> query(Configuration configuration, MappedStatement mappedStatement,Object ... objects) throws SQLException, ClassNotFoundException, NoSuchFieldException, IllegalAccessException, InstantiationException;

    void  insert(Configuration configuration, MappedStatement mappedStatement, Object... objects) throws SQLException, ClassNotFoundException, NoSuchFieldException, IllegalAccessException;

    void  update(Configuration configuration, MappedStatement mappedStatement, Object... objects) throws SQLException, ClassNotFoundException, NoSuchFieldException, IllegalAccessException;

    void  delete(Configuration configuration, MappedStatement mappedStatement, Object... objects) throws SQLException, ClassNotFoundException, NoSuchFieldException, IllegalAccessException;
}
