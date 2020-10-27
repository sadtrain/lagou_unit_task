package com.alias.dao;

import com.alias.entity.User;

import java.util.List;

public interface IUserDAO {
    List<User> selectAll();
    List<User> selectList(User user);
    User selectOne(User user);
    void insertByUser(User user);
    void updateByUser(User user);
    void deleteById(Integer userId);

}
