package com.example.controller;

import com.example.model.AuthenticationModel;
import com.example.model.CardModel;
import com.example.util.RedirectionHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;


/**
 * Created by Tomasz Wąsik, tomaszwasik@live.com
 */
@Controller
@RequestMapping(value = "/pin")
public class PinController implements DefaultController{

    private static final String REDIRECT_PREFIX = "redirect:";

    @Override
    public String showPage(Model model) {

        model.addAttribute("authentication", new AuthenticationModel());

        return "pin";
    }

    @PostMapping("/login")
    public String manageLoginAction(@ModelAttribute AuthenticationModel authenticationModel){

        System.out.println(authenticationModel.toString());

        return RedirectionHelper.showControllerPage(MenuController.class);
    }

    @RequestMapping(value = "/pin-change")
    public String showPinPage(){
        return "pin-change";
    }

}
