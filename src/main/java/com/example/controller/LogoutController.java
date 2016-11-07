package com.example.controller;

/**
 * Created by Tomasz Wąsik, tomaszwasik@live.com
 */

import com.example.util.RedirectionHelper;
import com.example.util.StaticField;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/logout")
public class LogoutController implements DefaultController{

    private static final String REDIRECT_PREFIX = "redirect:";

    @Override
    public String showPage(HttpServletRequest request, Model model) {

        if(request.getSession(false) == null){
            return RedirectionHelper.showControllerPage(HomeController.class);
        }

        model.addAttribute(StaticField.WITHDRAW_DESCRIPTION_KEY, "wypłacono 300 zł");

        return "logout";
    }

    @GetMapping("/approve")
    public String approveLogout(HttpServletRequest request, Model model) {

        request.getSession().invalidate();

        return RedirectionHelper.showControllerPage(HomeController.class);
    }
}
