package com.example.controller;

import com.example.bus.AuthenticationForm;
import com.example.bus.PinChangeForm;
import com.example.util.RedirectionHelper;
import com.example.util.StaticField;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;


/**
 * Created by Tomasz Wąsik, tomaszwasik@live.com
 */
@Controller
@RequestMapping(value = "/pin")
public class PinController {

    private static final String REDIRECT_PREFIX = "redirect:";

    @GetMapping
    public String showPage(HttpServletRequest request, Model model, boolean showErrorMessage) {

        if(request.getSession(false) == null){
            return RedirectionHelper.showControllerPage(HomeController.class);
        }

        model.addAttribute(StaticField.AUTHENTICATION_FORM_KEY, new AuthenticationForm());

        if(showErrorMessage){
            model.addAttribute(StaticField.ERROR_MESSAGE_KEY, "Błąd wprowadzonych danych! Proszę ponownie wprowadzić dane.");
        }

        return "pin";
    }

    @PostMapping("/login")
    public String manageLoginAction(@ModelAttribute AuthenticationForm authenticationForm, HttpServletRequest request, Model model){

        if(request.getSession(false) == null){
            return RedirectionHelper.showControllerPage(HomeController.class);
        }

        if(!isAuthenticationFormCorrect(authenticationForm)){
            return showPage(request, model, true);
        }

        System.out.println(authenticationForm.toString());

        return RedirectionHelper.showControllerPage(MenuController.class);
    }

    @GetMapping("/pin-change")
    public String showPinPage(HttpServletRequest request, Model model, boolean showErrorMessage){
        if(request.getSession(false) == null){
            return RedirectionHelper.showControllerPage(HomeController.class);
        }

        model.addAttribute(StaticField.PIN_CHANGE_FORMM_KEY, new PinChangeForm());

        if(showErrorMessage){
            model.addAttribute(StaticField.ERROR_MESSAGE_KEY, "Błąd wprowadzonych danych! Proszę ponownie wprowadzić dane.");
        }
        return "pin-change";
    }

    @PostMapping("/pin-change")
    public String changePin(HttpServletRequest request, Model model, @ModelAttribute PinChangeForm pinChangeForm){
        if(request.getSession(false) == null){
            return RedirectionHelper.showControllerPage(HomeController.class);
        }

        System.out.print(pinChangeForm.toString());

        if(!isPinChangeFormCorrect(pinChangeForm)){
            return showPinPage(request, model, true);
        }

        return RedirectionHelper.showControllerPage(LogoutController.class);
    }



    private boolean isAuthenticationFormCorrect(AuthenticationForm authenticationForm){

        if(authenticationForm == null || StringUtils.isBlank(authenticationForm.getAtmNumber())
                || authenticationForm.getCard() == null || StringUtils.isBlank(authenticationForm.getCard().getNumber())
                || StringUtils.isBlank(authenticationForm.getCard().getPin())){

            return false;
        }

        if(!StringUtils.isNumeric(authenticationForm.getAtmNumber())
                || !StringUtils.isNumeric(authenticationForm.getCard().getNumber())
                || !StringUtils.isNumeric(authenticationForm.getCard().getPin())){
            return false;
        }

        return true;
    }

    private boolean isPinChangeFormCorrect(PinChangeForm pinChangeForm){

        if(pinChangeForm == null
                || StringUtils.isBlank(pinChangeForm.getNewPin())
                || !StringUtils.isNumeric(pinChangeForm.getNewPin())){

            return false;
        }

        return true;
    }

}













