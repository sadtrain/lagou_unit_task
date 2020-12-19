package com.alias.springbootblog;

import com.alias.pojo.Article;
import com.alias.mapper.ArticleMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringbootBlogApplicationTests {

    @Autowired
    private ArticleMapper articleMapper;
    @Test
    void contextLoads() {
        Article byId = articleMapper.getById(1);
        System.out.println(byId.toString());
    }

}
