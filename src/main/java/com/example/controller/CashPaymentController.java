package com.example.controller;

import com.example.util.RedirectionHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Tomasz Wąsik, tomaszwasik@live.com
 */
@Controller
@RequestMapping(value = "/cash-payment")
public class CashPaymentController implements DefaultController{

    @Override
    public String showPage(Model model) {
        return "cash-payment";
    }

    @RequestMapping(value = "/withdraw-cash")
    public String showCashPaymentPage(){
        return RedirectionHelper.showControllerPage(CashPaymentController.class);
    }
}
