package com.alias.controller;


import com.alias.entity.User;
import com.alias.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RequestMapping("/login")
@Controller
public class LoginController {


    @Autowired
    private UserMapper userMapper;
    @RequestMapping("/toLogin")
    public String toLogin(){
        return "/login";
    }

    @RequestMapping("/login")
    public String login(HttpServletRequest request,String username,String password){
        HttpSession session = request.getSession();

        System.out.println(session.getId());
        User user = userMapper.selectOne(username);
        String failReason;
        if(user==null){
            return "redirect:/login/toError";
        }else{
            if(password.equals(user.getPassword())){
                session.setAttribute("username",username);
                session.setAttribute("password",password);
                return "redirect:/login/toResult";
            }else{
                return "redirect:/login/toError";
            }
        }
    }
    @RequestMapping("/toError")
    public String toError(){

        return "/error";
    }
    @RequestMapping("/toInfo")
    public String toInfo(){

        return "/userinfo";
    }
    @RequestMapping("/toResult")
    public String toResult(){

        return "/result";
    }
}
