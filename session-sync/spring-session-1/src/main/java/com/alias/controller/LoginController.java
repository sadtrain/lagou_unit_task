package com.alias.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RequestMapping("/login")
@Controller
public class LoginController {

    @RequestMapping("/toLogin")
    public String toLogin(){
        return "/login";
    }

    @RequestMapping("/login")
    public String login(HttpServletRequest request,String username,String password){
        HttpSession session = request.getSession();
        session.setAttribute("username",username);
        session.setAttribute("password",password);
        return "redirect:/login/toResult";
    }

    @RequestMapping("/toResult")
    public String toResult(){

        return "/result";
    }
}
