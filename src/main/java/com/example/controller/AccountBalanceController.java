package com.example.controller;

import com.example.util.RedirectionHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Tomasz Wąsik, tomaszwasik@live.com
 */
@Controller
@RequestMapping(value = "/account-balance")
public class AccountBalanceController implements DefaultController{
    @Override
    public String showPage(HttpServletRequest request, Model model) {
        if(request.getSession(false) == null){
            return RedirectionHelper.showControllerPage(HomeController.class);
        }

        return "account-balance";
    }
}
