package sqlsession;

import config.ConfigurationBuilder;
import io.Resources;
import pojo.Configuration;
import pojo.MappedStatement;

import java.io.InputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class SQLSession<T> {

    Configuration configuration;

    public SQLSession(Configuration configuration){
        this.configuration = configuration;
    }
    public Connection getConnection() throws SQLException {
        return configuration.getDataSource().getConnection();
    }
    public void insert(String statement,Object ... args) throws SQLException {
        SimpleExecutor simpleExecutor = new SimpleExecutor();

        MappedStatement mappedStatement = configuration.getMappedStatementMap().get(statement);
        try {
            simpleExecutor.insert(configuration,mappedStatement,args);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public List<T> selectList(String statement, Object ...arg) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        SimpleExecutor simpleExecutor = new SimpleExecutor();
        MappedStatement mappedStatement = configuration.getMappedStatementMap().get(statement);
        List<T> query = simpleExecutor.query(configuration, mappedStatement, arg);

        return query;
    }

    public void delete(String statement, Object ...arg) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        SimpleExecutor simpleExecutor = new SimpleExecutor();
        MappedStatement mappedStatement = configuration.getMappedStatementMap().get(statement);
        simpleExecutor.delete(configuration, mappedStatement, arg);
    }

    public void update(String statement, Object ...arg) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        SimpleExecutor simpleExecutor = new SimpleExecutor();
        MappedStatement mappedStatement = configuration.getMappedStatementMap().get(statement);
        simpleExecutor.update(configuration, mappedStatement, arg);
    }


    public static <T> T getMapper(Class<T> clazz){
        Class<?>[] interfaces = clazz.getInterfaces();
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, (proxy, method, args) -> {
            InputStream in = Resources.getResourceAsStream("SqlMapConfig.xml");
            Configuration configuration = new Configuration();
            ConfigurationBuilder configurationBuilder = new ConfigurationBuilder(configuration);
            configurationBuilder.parse(in);
            String statementId = clazz.getName()+"."+method.getName();
            MappedStatement mappedStatement = configuration.getMappedStatementMap().get(statementId);
            String sqlText = mappedStatement.getSqlText();
            String sqlType = mappedStatement.getSqlType();
//            mappedStatement.get
            SQLSession sqlSession = new SQLSessionFactory().getSQLSession(configuration);
            if("select".equals(sqlType)){
                return sqlSession.selectList(statementId,args) ;
            }else if("update".equals(sqlType)){
                sqlSession.update(statementId,args);
                return null;
            }else if("insert".equals(sqlType)){
                sqlSession.insert(statementId,args);
                return null;
            }else if("delete".equals(sqlType)){
                sqlSession.delete(statementId,args);
                return null;
            }else{
                throw new RuntimeException("sql标签你是不是写错了呢");
//                return null;
            }
//            Method method1 = SQLSession.class.getMethod(method.getName());
//            Object invoke = method1.invoke(sqlSession, args);
//            return invoke;
        });
//        ;
    }
}
