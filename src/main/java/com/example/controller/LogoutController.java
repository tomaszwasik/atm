package com.example.controller;

/**
 * Created by Tomasz Wąsik, tomaszwasik@live.com
 */

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

@Controller
@RequestMapping(value = "/logout")
public class LogoutController implements DefaultController{

    private static final String REDIRECT_PREFIX = "redirect:";

    @Override
    public String showPage(Model model) {

        return "logout";
    }

    @RequestMapping(value="/approve", method= RequestMethod.GET)
    public String approveLogout(Model model) {

        final UriComponentsBuilder uriComponents = MvcUriComponentsBuilder.fromController(HomeController.class);
        final String path = uriComponents.build().getPath();

        return REDIRECT_PREFIX+path;
    }
}
