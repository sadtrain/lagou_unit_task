package com.alias.mapper;

import com.alias.entity.User;
import org.springframework.stereotype.Repository;

/**
 * @Description :
 * @Author : ZGS
 * @Date: 2021-03-03 22:18
 */
@Repository
public interface UserMapper {

    User selectOne(String username);

}
