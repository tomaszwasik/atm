package com.example.controller;

import com.example.bus.SessionConfigurationModel;
import com.example.util.RedirectionHelper;
import com.example.util.StaticField;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by Tomasz Wąsik, tomaszwasik@live.com
 */

@Controller
@RequestMapping(value = "/language")
public class LanguageController implements DefaultController{

    private static Logger logger = Logger.getLogger(LanguageController.class);

    private static final String REDIRECT_PREFIX = "redirect:";

    @Override
    public String showPage(HttpServletRequest request, Model model) {

        request.getSession().invalidate();
        HttpSession session = request.getSession(true);
        SessionConfigurationModel sessionConfigurationModel = new SessionConfigurationModel();
        session.setAttribute(StaticField.SESSION_CONFIG_MODEL_KEY, sessionConfigurationModel);

        return "language";
    }

    @RequestMapping(value="/{language}", method= RequestMethod.GET)
    public String chooseLanguage(@PathVariable String language, HttpServletRequest request) {

        HttpSession session = request.getSession(false);

        if(session == null){
            return RedirectionHelper.showControllerPage(HomeController.class);
        }

        logger.info("Language: " + language);

        SessionConfigurationModel sessionConfigurationModel =(SessionConfigurationModel) session.getAttribute(StaticField.SESSION_CONFIG_MODEL_KEY);
        sessionConfigurationModel.setLanguage(language);
        session.setAttribute(StaticField.SESSION_CONFIG_MODEL_KEY, sessionConfigurationModel);


        return RedirectionHelper.showControllerPage(PinController.class);
    }

}
