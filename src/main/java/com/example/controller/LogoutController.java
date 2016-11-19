package com.example.controller;

/**
 * Created by Tomasz Wąsik, tomaszwasik@live.com
 */

import com.example.bus.SessionConfigurationModel;
import com.example.util.RedirectionHelper;
import com.example.util.AtmApplicationStatics;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/logout")
public class LogoutController {

    @GetMapping
    public String showPage(HttpServletRequest request, Model model) {

        if(request.getSession(false) == null){
            return RedirectionHelper.showControllerPage(HomeController.class);
        }
        SessionConfigurationModel attribute = (SessionConfigurationModel) request.getSession().getAttribute(AtmApplicationStatics.SESSION_CONFIG_MODEL_KEY);

        if(StringUtils.isNotBlank(attribute.getMessageForUser())){
            model.addAttribute(AtmApplicationStatics.DEFAULT_MESSAGE_KEY, attribute.getMessageForUser());
        }

        return "logout";
    }

    @GetMapping("/approve")
    public String approveLogout(HttpServletRequest request, Model model) {

        request.getSession().invalidate();

        return RedirectionHelper.showControllerPage(HomeController.class);
    }
}
