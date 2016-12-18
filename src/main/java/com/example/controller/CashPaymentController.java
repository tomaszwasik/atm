package com.example.controller;

import com.example.bus.CashPaymentForm;
import com.example.bus.SessionConfigurationModel;
import com.example.model.ATMDenomination;
import com.example.model.Account;
import com.example.service.ATMDenominationService;
import com.example.service.AccountService;
import com.example.util.RedirectionHelper;
import com.example.util.AtmApplicationStatics;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Tomasz Wąsik, tomaszwasik@live.com
 */
@Controller
@RequestMapping(value = "/cash-payment")
public class CashPaymentController {

    @Autowired
    private ATMDenominationService atmDenominationService;

    @Autowired
    private AccountService accountService;

    @GetMapping
    public String showPage(HttpServletRequest request, Model model) {

        if(request.getSession(false) == null){
            return RedirectionHelper.showControllerPage(HomeController.class);
        }

        //sprawdzenie kwoty
        double minAtmDenomination = findMinAtmDenomination(request);

        if(!validateAtmBalance(request) || minAtmDenomination == Double.MAX_VALUE){
            return showPageWithError(request, model, "Wypłata gotówki jest niemożliwa.");
        }

        model.addAttribute("denominationInfo", "Bankomant wypłaca wielokrotność " + minAtmDenomination + " zł.");

        model.addAttribute(AtmApplicationStatics.CASH_PAYMENT_FORM_KEY, new CashPaymentForm());

        return "cash-payment";
    }

    private String showPageWithError(HttpServletRequest request, Model model, String errorMessage){
        if(request.getSession(false) == null){
            return RedirectionHelper.showControllerPage(HomeController.class);
        }

        model.addAttribute(AtmApplicationStatics.CASH_PAYMENT_FORM_KEY, new CashPaymentForm());

        if(StringUtils.isNotBlank(errorMessage)){
            model.addAttribute(AtmApplicationStatics.ERROR_MESSAGE_KEY, errorMessage);
        }

        return "cash-payment";
    }

    @PostMapping("/withdraw-cash")
    public String showCashPaymentPage(@ModelAttribute CashPaymentForm cashPaymentForm, HttpServletRequest request, Model model){

        if(request.getSession(false) == null){
            return RedirectionHelper.showControllerPage(HomeController.class);
        }

        System.out.print(cashPaymentForm.toString());

        double minAtmDenomination = findMinAtmDenomination(request);
        if(!validateAtmBalance(request) || minAtmDenomination == Double.MAX_VALUE){
            return showPageWithError(request, model, "Wypłata gotówki jest niemożliwa.");
        }

        if(!isCashPaymentFormCorrect(request, cashPaymentForm)){
            return showPageWithError(request, model, AtmApplicationStatics.DEFAULT_DATA_ERROR_MSG);
        }

        // TUTAJ zawołać integrację z bazą!!!!!!!!!!!!!!!!!

        return RedirectionHelper.showControllerPage(LogoutController.class);
    }

    private boolean isCashPaymentFormCorrect(HttpServletRequest request, CashPaymentForm cashPaymentForm){

        if(cashPaymentForm == null
                || StringUtils.isBlank(cashPaymentForm.getAmount())
                || !StringUtils.isNumeric(cashPaymentForm.getAmount())){
            return false;
        }

        double minAtmDenomination = findMinAtmDenomination(request);
        SessionConfigurationModel sessionConfigurationModel =(SessionConfigurationModel) request.getSession().getAttribute(AtmApplicationStatics.SESSION_CONFIG_MODEL_KEY);
        Account atmAccount = accountService.findAccountById(sessionConfigurationModel.getAtmAccountId());

        if(minAtmDenomination == Double.MAX_VALUE
                || Double.valueOf(cashPaymentForm.getAmount()).doubleValue() % minAtmDenomination != Double.valueOf(0).doubleValue()
                || Double.valueOf(cashPaymentForm.getAmount()).doubleValue() > atmAccount.getAtmSaldo().doubleValue()){

            return false;
        }
        return true;
    }


    private boolean validateAtmBalance(HttpServletRequest request){

        SessionConfigurationModel sessionConfigurationModel =(SessionConfigurationModel) request.getSession().getAttribute(AtmApplicationStatics.SESSION_CONFIG_MODEL_KEY);
        Account atmAccount = accountService.findAccountById(sessionConfigurationModel.getAtmAccountId());

        // sprawdzenie bilansu
        BigDecimal atmSaldo = atmAccount.getAtmSaldo();
        if(atmSaldo == null || atmSaldo.doubleValue() < Double.valueOf(10.00)){
            return false;
        }
        return true;
    }


    private double findMinAtmDenomination(HttpServletRequest request){
        SessionConfigurationModel sessionConfigurationModel =(SessionConfigurationModel) request.getSession().getAttribute(AtmApplicationStatics.SESSION_CONFIG_MODEL_KEY);
        Account atmAccount = accountService.findAccountById(sessionConfigurationModel.getAtmAccountId());

        // sprawdzenie min nominału
        double minDenomination = Double.MAX_VALUE;

        List<ATMDenomination> atmDenominationByAccountId = atmDenominationService.findATMDenominationsByAccountId(sessionConfigurationModel.getAtmAccountId());

        for(ATMDenomination atmDenomination : atmDenominationByAccountId){

            double denomination = atmDenomination.getDenominationCurrency().getDenomination().getDenomination().doubleValue();

            if(atmDenomination.getQuantity() >= 5 && minDenomination > denomination){
                minDenomination = Double.valueOf(denomination);
            }
        }

        return minDenomination;
    }
}















