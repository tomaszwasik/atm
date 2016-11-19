package com.example.controller;

import com.example.bus.SessionConfigurationModel;
import com.example.model.Account;
import com.example.service.AccountService;
import com.example.util.AtmApplicationStatics;
import com.example.util.RedirectionHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Tomasz Wąsik, tomaszwasik@live.com
 */
@Controller
@RequestMapping(value = "/account-balance")
public class AccountBalanceController implements DefaultController{

    @Autowired
    private AccountService accountService;

    @Override
    public String showPage(HttpServletRequest request, Model model) {
        if(request.getSession(false) == null){
            return RedirectionHelper.showControllerPage(HomeController.class);
        }

        SessionConfigurationModel attribute = (SessionConfigurationModel) request.getSession().getAttribute(AtmApplicationStatics.SESSION_CONFIG_MODEL_KEY);

        Account userAccount = accountService.findAccountById(attribute.getUserAccountId());



        model.addAttribute(AtmApplicationStatics.AMOUNT_MSG_KEY,
                String.valueOf(userAccount.getCurrentMoney().doubleValue()).replace(".", ",")
                        + " "
                        + userAccount.getCurrency().getSymbol());

        return "account-balance";
    }
}
