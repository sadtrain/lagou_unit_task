//package com.alias.dao.impl;
//
//import com.alias.dao.IUserDAO;
//import com.alias.entity.User;
//import config.ConfigurationBuilder;
//import io.Resources;
//import org.dom4j.DocumentException;
//import pojo.Configuration;
//import sqlsession.SQLSession;
//import sqlsession.SQLSessionFactory;
//
//import java.beans.PropertyVetoException;
//import java.io.InputStream;
//import java.sql.SQLException;
//import java.util.List;
//
//public class UserDAOImpl implements IUserDAO {
//    public List<User> selectList(User user) {
//        InputStream in = Resources.getResourceAsStream("SqlMapConfig.xml");
//        Configuration configuration = new Configuration();
//        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder(configuration);
//        try {
//            configurationBuilder.parse(in);
//        } catch (DocumentException e) {
//            e.printStackTrace();
//        } catch (PropertyVetoException e) {
//            e.printStackTrace();
//        }
//        try {
//            SQLSession sqlSession = new SQLSessionFactory().getSQLSession(configuration);
//            List<User> users = sqlSession.selectList("userMapper.selectById", new User(1, "张三", 18));
//            return users;
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//        } catch (NoSuchFieldException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    public User selectOne(User user) {
//        return null;
//    }
//
//    public void insert(User user) {
//
//    }
//}
