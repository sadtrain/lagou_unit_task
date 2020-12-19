package com.alias.mapper;

import com.alias.pojo.Article;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ArticleMapper {

    @Select("select * from t_article where id = #{id}")
    Article getById(Integer id);

    @Select("select * from t_article limit #{start} ,#{offset}")
    List<Article> getByPage(Integer start, Integer offset);

    @Select("select count(*) from t_article")
    Integer countArticle();

}
