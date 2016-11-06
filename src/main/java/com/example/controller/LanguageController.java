package com.example.controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Created by Tomasz Wąsik, tomaszwasik@live.com
 */

@Controller
@RequestMapping(value = "/language")
public class LanguageController implements DefaultController{

    private static Logger logger = Logger.getLogger(LanguageController.class);

    private static final String REDIRECT_PREFIX = "redirect:";

    @Override
    public String showPage(Model model) {
        return "language";
    }

    @RequestMapping(value="/{language}", method= RequestMethod.GET)
    public String chooseLanguage(@PathVariable String language) {

        logger.info("Language: " + language);

        final UriComponentsBuilder uriComponents = MvcUriComponentsBuilder.fromController(PinController.class);
        final String path = uriComponents.build().getPath();

        logger.info("Redirecting to: [" + REDIRECT_PREFIX + path + "].");

        return REDIRECT_PREFIX+path;
    }

}
