package com.example.controller;

import com.example.bus.SessionConfigurationModel;
import com.example.util.AtmApplicationStatics;
import com.example.util.RedirectionHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Tomasz Wąsik, tomaszwasik@live.com
 */
@Controller
@RequestMapping(value = "/menu")
public class MenuController implements DefaultController {


    @Override
    public String showPage(HttpServletRequest request, Model model) {
        if(request.getSession(false) == null){
            return RedirectionHelper.showControllerPage(HomeController.class);
        }

        return "menu";
    }
}
