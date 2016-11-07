package com.example.controller;

import com.example.bus.AuthenticationForm;
import com.example.bus.CashPaymentForm;
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
@RequestMapping(value = "/cash-payment")
public class CashPaymentController {

    @GetMapping
    public String showPage(HttpServletRequest request, Model model, boolean showErrorMessage) {

        if(request.getSession(false) == null){
            return RedirectionHelper.showControllerPage(HomeController.class);
        }

        model.addAttribute(StaticField.CASH_PAYMENT_FORM_KEY, new CashPaymentForm());

        if(showErrorMessage){
            model.addAttribute(StaticField.ERROR_MESSAGE_KEY, "Błąd wprowadzonych danych! Proszę ponownie wprowadzić dane.");
        }

        return "cash-payment";
    }

    @PostMapping("/withdraw-cash")
    public String showCashPaymentPage(@ModelAttribute CashPaymentForm cashPaymentForm, HttpServletRequest request, Model model){

        System.out.print(cashPaymentForm.toString());

        if(!isCashPaymentFormCorrect(cashPaymentForm)){
            return showPage(request, model, true);
        }

        return RedirectionHelper.showControllerPage(LogoutController.class);
    }

    private boolean isCashPaymentFormCorrect(CashPaymentForm cashPaymentForm){

        if(cashPaymentForm == null
                || StringUtils.isBlank(cashPaymentForm.getAmount())
                || !StringUtils.isNumeric(cashPaymentForm.getAmount())){
            return false;
        }
        return true;
    }
}















