package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by Tomasz Wąsik, tomaszwasik@live.com
 */
@Controller
@RequestMapping(value = "/")
public class HomeController  implements DefaultController{

    @Override
    public String showPage(HttpServletRequest request, Model model) {

        request.getSession().invalidate();

        return "home";
    }
}
