package com.alias;

import com.alias.dao.IUserDAO;
import com.alias.entity.User;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import config.ConfigurationBuilder;
import io.Resources;
import org.dom4j.DocumentException;
import org.junit.Test;
import pojo.Configuration;
import sqlsession.SQLSession;
import sqlsession.SQLSessionFactory;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws PropertyVetoException, DocumentException, SQLException {
        InputStream in = Resources.getResourceAsStream("SqlMapConfig.xml");
        Configuration configuration = new Configuration();
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder(configuration);
        configurationBuilder.parse(in);
        SQLSession sqlSession = new SQLSessionFactory().getSQLSession(configuration);
        sqlSession.insert("userMapper.insertByUser",new User(1,"张三",18));


//        System.out.println(configuration.getDataSource()+""+configuration.getMappedStatementMap());
//        ComboPooledDataSource dataSource = new ComboPooledDataSource();
//        dataSource.setDriverClass("com.mysql.jdbc.Driver");
//        dataSource.setJdbcUrl("jdbc:mysql://127.0.0.1/lagou_mybatis");
//        dataSource.setPassword("427699");
//        dataSource.setUser("root");
//        Connection connection = dataSource.getConnection();
//        connection.close();
    }
    @Test
    public void testQuery() {
        IUserDAO userDAO = SQLSession.getMapper(IUserDAO.class);
        List<User> users = userDAO.selectAll();
        for (User user : users) {
            System.out.println(user.getName());
        }
//        InputStream in = Resources.getResourceAsStream("SqlMapConfig.xml");
//        Configuration configuration = new Configuration();
//        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder(configuration);
//        configurationBuilder.parse(in);
//        SQLSession sqlSession = new SQLSessionFactory().getSQLSession(configuration);
//        List<User> users = sqlSession.selectList("userMapper.selectById", new User(1, "张三", 18));
//        for (User user : users) {
//            System.out.println(user.getName());
//        }
    }
    @Test
    public void testInsert(){
        IUserDAO userDAO = SQLSession.getMapper(IUserDAO.class);
        userDAO.insertByUser(new User(4, "wangwu", 18));
        List<User> users = userDAO.selectAll();
        for (User user : users) {
            System.out.println(user.getName());
        }
    }
    @Test
    public void testUpdate(){
        IUserDAO userDAO = SQLSession.getMapper(IUserDAO.class);
        userDAO.updateByUser(new User(4, "wangwu", 19));
        List<User> users = userDAO.selectAll();
        for (User user : users) {
            System.out.println(user.getName());
        }
    }
    @Test
    public void testDelete(){
        IUserDAO userDAO = SQLSession.getMapper(IUserDAO.class);
        userDAO.deleteById(2);
        List<User> users = userDAO.selectAll();
        for (User user : users) {
            System.out.println(user.getName());
        }
    }
}
