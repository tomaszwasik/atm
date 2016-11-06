package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Tomasz Wąsik, tomaszwasik@live.com
 */
@Controller
@RequestMapping(value = "/menu")
public class MenuController implements DefaultController {


    @Override
    public String showPage(Model model) {

        return "menu";
    }
}
