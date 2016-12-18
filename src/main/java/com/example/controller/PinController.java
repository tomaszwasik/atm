package com.example.controller;

import com.example.bus.AuthenticationForm;
import com.example.bus.PinChangeForm;
import com.example.bus.SessionConfigurationModel;
import com.example.model.Account;
import com.example.model.Card;
import com.example.service.AccountService;
import com.example.service.CardService;
import com.example.util.RedirectionHelper;
import com.example.util.AtmApplicationStatics;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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

    private static Logger logger = Logger.getLogger(PinController.class);

    private static final String REDIRECT_PREFIX = "redirect:";
    private static final String DEFAULT_ERROR_MESSAGE = "Błąd wprowadzonych danych! Proszę ponownie wprowadzić dane.";

    @Autowired
    private AccountService accountService;

    @Autowired
    private CardService cardService;

    @GetMapping
    public String showPage(HttpServletRequest request, Model model, String errorMessage) {

        if(request.getSession(false) == null){return RedirectionHelper.showControllerPage(HomeController.class);}
        model.addAttribute(AtmApplicationStatics.AUTHENTICATION_FORM_KEY, new AuthenticationForm());

        if(StringUtils.isNotBlank(errorMessage)){model.addAttribute(AtmApplicationStatics.ERROR_MESSAGE_KEY, errorMessage);}

        return "pin";
    }

    @PostMapping("/login")
    public String manageLoginAction(@ModelAttribute AuthenticationForm authenticationForm, HttpServletRequest request, Model model){

        if(request.getSession(false) == null){
            return RedirectionHelper.showControllerPage(HomeController.class);
        }

        logger.info(authenticationForm.toString());

        if(!isAuthenticationFormCorrect(authenticationForm)){
            return showPage(request, model, DEFAULT_ERROR_MESSAGE);
        }

        // check ATM
        Account atm = accountService.findAccountById(Long.parseLong(authenticationForm.getAtmNumber()));
        if(atm == null || !atm.isAtm()){
            return showPage(request, model, DEFAULT_ERROR_MESSAGE);
        }


        // check card number and pin
        Card card = cardService.findCardById(Long.parseLong(authenticationForm.getCard().getNumber()));

        if(card == null || !StringUtils.equals(authenticationForm.getCard().getPin(), card.getPin())){
            return showPage(request, model, DEFAULT_ERROR_MESSAGE);
        }

        SessionConfigurationModel sessionConfigModel =(SessionConfigurationModel) request.getSession().getAttribute(AtmApplicationStatics.SESSION_CONFIG_MODEL_KEY);

        // check if card was stolen
        if(card.isStolen()){

            sessionConfigModel.setMessageForUser(AtmApplicationStatics.CARD_WAS_STOLEN_INFO);
            request.getSession().setAttribute(AtmApplicationStatics.SESSION_CONFIG_MODEL_KEY, sessionConfigModel);
            return RedirectionHelper.showControllerPage(LogoutController.class);
        }

        sessionConfigModel.setAtmAccountId(atm.getId());
        logger.info("ATM account id: " + atm.getId());

        sessionConfigModel.setUserAccountId(card.getAccount().getId());
        logger.info("User account id: " + card.getAccount().getId());

        sessionConfigModel.setUserCardId(card.getId());
        logger.info("User card id: " + card.getId());

        request.getSession().setAttribute(AtmApplicationStatics.SESSION_CONFIG_MODEL_KEY, sessionConfigModel);

        return RedirectionHelper.showControllerPage(MenuController.class);
    }

    @GetMapping("/pin-change")
    public String showPinPage(HttpServletRequest request, Model model, boolean showErrorMessage){
        if(request.getSession(false) == null){
            return RedirectionHelper.showControllerPage(HomeController.class);
        }

        model.addAttribute(AtmApplicationStatics.PIN_CHANGE_FORM_KEY, new PinChangeForm());

        if(showErrorMessage){
            model.addAttribute(AtmApplicationStatics.ERROR_MESSAGE_KEY, "Błąd wprowadzonych danych! Proszę ponownie wprowadzić dane.");
        }
        return "pin-change";
    }

    @PostMapping("/pin-change")
    public String changePin(HttpServletRequest request, Model model, @ModelAttribute PinChangeForm pinChangeForm){
        if(request.getSession(false) == null){
            return RedirectionHelper.showControllerPage(HomeController.class);
        }
        logger.info(pinChangeForm.toString());

        SessionConfigurationModel attribute = (SessionConfigurationModel) request.getSession().getAttribute(AtmApplicationStatics.SESSION_CONFIG_MODEL_KEY);
        Card userCard = cardService.findCardById(attribute.getUserCardId());


        if(!isPinChangeFormCorrect(pinChangeForm) || StringUtils.equals(pinChangeForm.getNewPin(), userCard.getPin())){
            return showPinPage(request, model, true);
        }

        userCard.setPin(pinChangeForm.getNewPin());
        cardService.updateCard(userCard);

        attribute.setMessageForUser(AtmApplicationStatics.PIN_CHANGE_INFO);

        request.getSession().setAttribute(AtmApplicationStatics.SESSION_CONFIG_MODEL_KEY, attribute);

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
                || !StringUtils.isNumeric(authenticationForm.getCard().getPin())
                || StringUtils.length(authenticationForm.getCard().getPin()) != 4){
            return false;
        }
        return true;
    }

    private boolean isPinChangeFormCorrect(PinChangeForm pinChangeForm){
        if(pinChangeForm == null
                || StringUtils.isBlank(pinChangeForm.getNewPin())
                || !StringUtils.isNumeric(pinChangeForm.getNewPin())
                || StringUtils.length(pinChangeForm.getNewPin()) != 4){
            return false;
        }
        return true;
    }
}













