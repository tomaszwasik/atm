package com.example.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by Tomasz Wąsik, tomaszwasik@live.com
 */
@FunctionalInterface
public interface DefaultController {

    @GetMapping
    String showPage(Model model);
}
