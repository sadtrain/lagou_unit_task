package com.alias.controller;

import com.alias.mapper.ArticleMapper;
import com.alias.pojo.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/article")
public class ArticleController {


    @Autowired
    private ArticleMapper articleMapper;
    @RequestMapping("/index")
    public String index(Integer pageNo, Integer pageSize, Model model){
        List<Article> articles;
        Integer count;
        if(pageNo==null||pageSize==null) {
            pageNo = 1;
            pageSize = 3;

        }
        articles = articleMapper.getByPage((pageNo - 1) * pageSize, pageSize);
        count = articleMapper.countArticle();
        model.addAttribute("articles",articles);
        model.addAttribute("pageNo",pageNo);
        model.addAttribute("pageSize",pageSize);
        model.addAttribute("total",count);
//        ModelAndView modelAndView = new ModelAndView("client/index");
//        modelAndView.addObject("articles",articles);
//        return new ModelAndView("client/index");
        return "client/index";



    };
}
